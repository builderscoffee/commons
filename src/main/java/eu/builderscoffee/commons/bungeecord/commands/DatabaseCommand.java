package eu.builderscoffee.commons.bungeecord.commands;

import eu.builderscoffee.commons.bungeecord.Main;
import eu.builderscoffee.commons.bungeecord.utils.*;
import lombok.SneakyThrows;
import lombok.val;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.command.ConsoleCommandSender;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

public class DatabaseCommand extends Command {

    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    /*private static final TreeSet<Class<?>> listable = new TreeSet<>(Comparator.comparing(Class::getSimpleName));
    private static final TreeSet<Class<?>> addable = new TreeSet<>(Comparator.comparing(Class::getSimpleName));
    private static final TreeSet<Class<?>> updatable = new TreeSet<>(Comparator.comparing(Class::getSimpleName));*/

    public DatabaseCommand() {
        super("database", Main.getInstance().getPermissions().getDatabasePermission(), "db");
    }

    /*/***
     * Permet la manipulation de la table via la commande /database
     * @param clazz Table Class of requery
     */
    /*public static <T> void allowCommandManipulation(Class<T> clazz) {
        if(!clazz.isAnnotationPresent(EntityRefference.class))
            throw new RuntimeException("Tried to load a non-annotated table");
        if(clazz.isAnnotationPresent(Listable.class))
            listable.add(clazz);
        if(clazz.isAnnotationPresent(Addable.class))
            addable.add(clazz);
        if(clazz.isAnnotationPresent(Updatable.class))
            updatable.add(clazz);
    }

    @SneakyThrows
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission(Main.getInstance().getPermissions().getDatabasePermission())){
            sender.sendMessage(TextComponentUtil.decodeColor(Main.getInstance().getMessages().getNoPermission()));
            return;
        }

        String argsToString = "";
        for (String arg : args) {
            argsToString += " " + arg;
        }
        System.out.println(sender.getName() + " issued proxy command: /database" + argsToString);

        val arg0 = argument(args, 1);
        val arg1 = argument(args, 2);
        val arg2 = argument(args, 3);
        val arg3 = argument(args, 4);

        try{
            if(arg0.equalsIgnoreCase("list") || arg0.equalsIgnoreCase("l")){
                // Check si le type est mit
                if(args.length < 2){
                    sender.sendMessage(TextComponentUtil.decodeColor("§6/database list §e<type>"));
                    return;
                }

                // Récuperer la class selon le nom de la table donné
                Class<?> classTable = null;
                for (Class<?> aClass : listable) {
                    if(aClass.getSimpleName().equalsIgnoreCase(arg1)){
                        classTable = aClass;
                    }
                }

                // Check si la class de table existe
                if(classTable == null){
                    sender.sendMessage(TextComponentUtil.decodeColor("§cLa table \"§f" + arg1 + "§c\" n'es pas trouvé !"));
                    String options = "";
                    for (Class<?> varClass : listable) {
                        options += (!options.toString().equals("") ? "§c, §f" : "§f") + varClass.getSimpleName().toLowerCase();
                    }
                    sender.sendMessage(TextComponentUtil.decodeColor("§cChoisisez entre: " + options));
                    return;
                }

                // Récupère la class entité de la table et les annotations
                val classEntity = classTable.getAnnotation(EntityRefference.class).entityClass();
                val anotation = classTable.getAnnotation(Listable.class);

                // Check si le joueur a mis show ou filtre
                if(args.length < 3
                        || (!arg2.equalsIgnoreCase("show")
                        && !arg2.equalsIgnoreCase("filter")
                        && !arg2.equalsIgnoreCase("variables"))) {
                    sender.sendMessage(TextComponentUtil.decodeColor("§6/database list " + arg1 + " §eshow [page]"));
                    sender.sendMessage(TextComponentUtil.decodeColor("§6/database list " + arg1 + " §evariables"));
                    sender.sendMessage(TextComponentUtil.decodeColor("§6/database list " + arg1 + " §efilter [<variable>.<condition>.<value>] [chown variable,...] [page]"));
                    sender.sendMessage(TextComponentUtil.decodeColor("\n"));
                    sender.sendMessage(TextComponentUtil.decodeColor("Example:"));
                    sender.sendMessage(TextComponentUtil.decodeColor("§7/database list profil filter name.equal.xelonmusk name,name 3"));
                    return;
                }

                int page = 1;

                // Initialise le début de la requete
                List<String> conditions = new ArrayList<>();
                List<String> fieldExpressions = new ArrayList<>();

                if(arg2.equalsIgnoreCase("variables")){
                    sender.sendMessage(TextComponentUtil.decodeColor("§6Variables disponibles dans la table \"§f" + arg1 + "§6\":"));
                    sender.sendMessage(TextComponentUtil.decodeColor(getFieldsStringFromClass(classEntity)));
                    return;
                }
                else if(arg2.equalsIgnoreCase("filter")){
                    // Check si quel que chose d'autre vient après le "filtre"
                    if(args.length < 4){
                        sender.sendMessage(TextComponentUtil.decodeColor("§cUn argument minimum est requis !"));
                        return;
                    }
                    // Boucle les argument autres
                    for(int i = 3; i < args.length; i++){

                        // TODO Faire de tous ce qu'il y a dans la boucle for une fonction pour réutiliser dans le update et delete
                        // Si c'est une condition (avec le point -> .)
                        if(args[i].contains(".")){
                            String[] conditionString = args[i].split("\\.");

                            // Check si il y a 3 argument à la condition
                            if(conditionString.length != 3){
                                sender.sendMessage(TextComponentUtil.decodeColor("§cLa condition \"" + args[i] + "\" est incorrecte !"));
                                return;
                            }

                            // Met la valeur de la condition
                            String value = conditionString[2];

                            // Vérifie si la valeur contien un guimet au cas ou il faut accepter les espaces
                            if(value.contains("\"")){
                                int quoteFound = 0;
                                String argsToEndOfQuote = "";
                                for(int j = i; j < args.length; j++){
                                    argsToEndOfQuote += (!argsToEndOfQuote.toString().equals("") ? " " : "") + args[j];
                                    if(args[j].contains("\"")){
                                        quoteFound += args[j].length() - args[j].replaceAll("\"","").length();
                                        i = j;
                                        if(quoteFound == 2) break;
                                    }
                                }
                                // Vérifie si il y a une fin
                                if(quoteFound < 2){
                                    sender.sendMessage(TextComponentUtil.decodeColor("§cLa valeur de la condition doit au moins terminer par un guimet!"));
                                    return;
                                }
                                else if(quoteFound > 2){
                                    sender.sendMessage(TextComponentUtil.decodeColor("§cLa valeur de la condition contient trop de guimets!"));
                                    return;
                                }

                                // Sépare entre le avant, dedans et après les guimets
                                // Example:
                                // avant"dedans"après
                                val split = argsToEndOfQuote.toString().split("\"");
                                val stringWithoutCondition = split[0].substring(conditionString[0].length() + conditionString[1].length() + 2);
                                if(stringWithoutCondition.length() > 0){
                                    sender.sendMessage(TextComponentUtil.decodeColor("§cRien ne peut être mis avant le permier guimet (§f" + stringWithoutCondition + "§c)"));
                                    return;
                                }
                                if(split.length == 3 && split[2].length() > 0){
                                    sender.sendMessage(TextComponentUtil.decodeColor("§cRien ne peut être mis après le deuxieme guimet (§f" + split[2] + "§c)"));
                                    return;
                                }
                                value = split[1];
                            }

                            String condition = null;
                            Field field = getFieldFromClass(classEntity, conditionString[0]);

                            if(field == null){
                                sender.sendMessage(TextComponentUtil.decodeColor("§cLa variable \"" + conditionString[0] + "\" n'existe pas dans \"" + classEntity.getSimpleName() + "\" !"));
                                sender.sendMessage(TextComponentUtil.decodeColor(getFieldsStringFromClass(classEntity)));
                                return;
                            }

                            Attribute<?,?> attribute = (Attribute<?,?>)field.get(null);
                            if(attribute.getClassType().equals(Integer.class) || attribute.getClassType().equals(int.class)){
                                // Check si le joueur a bien mis les bonnes conditions
                                if(!conditionString[1].equalsIgnoreCase("equal")
                                        && !conditionString[1].equalsIgnoreCase("greater")
                                        && !conditionString[1].equalsIgnoreCase("less"))
                                {
                                    sender.sendMessage(TextComponentUtil.decodeColor("§cDes chiffres peuvent qu'avoir comme condition: equal, greater ou less"));
                                    return;
                                }

                                // Check si la valeur donné est un chiffre
                                if(!pattern.matcher(value).matches()){
                                    sender.sendMessage(TextComponentUtil.decodeColor("§c\"" + value + "\" n'est pas un chiffre !"));
                                    return;
                                }

                                if(conditionString[1].equalsIgnoreCase("equal")){
                                    condition = attribute.toString() + " = " + Integer.parseInt(value);
                                    //condition = NamedExpression.ofInteger(attribute.toString()).eq(Integer.parseInt(value));
                                }
                                else if(conditionString[1].equalsIgnoreCase("greater")){
                                    condition = attribute.toString() + " > " + Integer.parseInt(value);
                                    //condition = NamedExpression.ofInteger(attribute.toString()).greaterThan(Integer.parseInt(value));
                                }
                                else if(conditionString[1].equalsIgnoreCase("less")){
                                    condition = attribute.toString() + " < " + Integer.parseInt(value);
                                    //condition = NamedExpression.ofInteger(attribute.toString()).lessThan(Integer.parseInt(value));
                                }
                            }
                            // Check si la variable est un text (String)
                            else if(attribute.getClassType().equals(String.class)){
                                // Check si le joueur a bien mis les bonnes conditions
                                if(!conditionString[1].equalsIgnoreCase("equal")
                                        && !conditionString[1].equalsIgnoreCase("like"))
                                {
                                    sender.sendMessage(TextComponentUtil.decodeColor("§cDu text ne peut qu'avoir comme condition: equal ou like"));
                                    return;
                                }

                                if(conditionString[1].equalsIgnoreCase("equal")){
                                    condition = attribute.toString() + " = '" + (value.contains("'")? value.replace("'", "\\'") : value) + "'";
                                    //condition = NamedExpression.ofString(attribute.toString()).equalsIgnoreCase(value);
                                }
                                else if(conditionString[1].equalsIgnoreCase("like")){
                                    condition = attribute.toString() + " LIKE '" + (value.contains("'")? value.replace("'", "\\'") : value) + "'";
                                    //condition = NamedExpression.ofString(attribute.toString()).like(value);
                                }
                            }
                            // Check si la variable est un text (String)
                            else if(attribute.getClassType().equals(Boolean.class) || attribute.getClassType().equals(boolean.class)){
                                // Check si le joueur a bien mis les bonnes conditions
                                if(!conditionString[1].equalsIgnoreCase("equal"))
                                {
                                    sender.sendMessage(TextComponentUtil.decodeColor("§cUn boolean ne peut qu'avoir comme condition: equal"));
                                    return;
                                }

                                if(conditionString[1].equalsIgnoreCase("equal")){
                                    if(value.equalsIgnoreCase("true")){
                                        condition = attribute.toString() + " = " + 1;
                                    }
                                    else if(value.equalsIgnoreCase("false")){
                                        condition = attribute.toString() + " = " + 0;
                                    }
                                    else{
                                        sender.sendMessage(TextComponentUtil.decodeColor("§c\"" + conditionString[0] + "\" est un boolean. Par conséquent il ne peut qu'être \"§ftrue§c\" ou \"§ffalse§c\""));
                                        return;
                                    }
                                }
                            }
                            // La variable est autre qu'un text ou un chiffre ou un boolean
                            else{
                                sender.sendMessage(TextComponentUtil.decodeColor("§c" + attribute.getClassType() + " n'est pas encore implémenté dans cette version !"));
                                return;
                            }



                            if(condition == null){
                                if(classEntity.getSimpleName().contains("Entity")){
                                    sender.sendMessage(TextComponentUtil.decodeColor("§cVotre condition est incomplete!"));
                                    sender.sendMessage(TextComponentUtil.decodeColor("§cVous devez préciser ce que vous voulez vérifier de §f" + conditionString[0]));
                                    sender.sendMessage(TextComponentUtil.decodeColor("§cVoici les possibilités que vous pouvez récupérer:"));
                                    sender.sendMessage(TextComponentUtil.decodeColor("§c" + getFieldsStringFromClass(classEntity)));
                                }
                            }
                            conditions.add(condition);
                        }
                        // Permet de changer de page
                        else if(pattern.matcher(args[i]).matches()){
                            page = Integer.parseInt(args[i]);
                        }
                        // Permet d'ajouter des atributs d'affichage
                        else{
                            for (String s : args[i].split(",")) {
                                Field field = getFieldFromClass(classEntity, s);

                                if(field == null){
                                    sender.sendMessage(TextComponentUtil.decodeColor("§cLa variable \"" + s + "\" n'existe pas dans \"" + classEntity.getSimpleName() + "\" !"));
                                    sender.sendMessage(TextComponentUtil.decodeColor(getFieldsStringFromClass(classEntity)));
                                    return;
                                }

                                Attribute<?,?> attribute = (Attribute<?,?>)field.get(null);
                                fieldExpressions.add(attribute.toString());
                            }
                        }
                    }
                }
                else{
                    // Permet de changer de page
                    if(args.length > 3 && pattern.matcher(arg3).matches()){
                        page = Integer.parseInt(arg3);
                    }
                }

                // Vérifie si des atributs ont déja été donné
                if(fieldExpressions.isEmpty()){
                    for(String fieldExpression : anotation.defaultVariableName()) {
                        fieldExpressions.add(((Type<?>) classEntity.getField("$TYPE").get(null)).getName() + "." + fieldExpression);
                    }
                }
                String fieldExpressionsString = "";
                for(String fieldExpression : fieldExpressions) {
                    fieldExpressionsString += (fieldExpressionsString.toString() != "" ? ", " : "") + fieldExpression;
                }

                String query = "SELECT " + fieldExpressionsString + " FROM " + ((Type<?>) classEntity.getField("$TYPE").get(null)).getName();

                if(!conditions.isEmpty()){
                    query += " WHERE " + conditions.get(0);
                    if(conditions.size() > 1){
                        for(int i = 1; i < conditions.size(); i++){
                            query += " AND " + conditions.get(i);
                        }
                    }
                }

                query += " ORDER BY " + fieldExpressions.get(0) + " ASC";

                val statement = Main.getInstance().getHikari().getConnection().createStatement();
                try {
                    val result = statement.executeQuery(query.toString());

                    result.last();
                    int count = result.getRow();

                    if (count == 0) {
                        sender.sendMessage(TextComponentUtil.decodeColor("§cAucune donnée"));
                        return;
                    }

                    if (page < 1) {
                        sender.sendMessage(TextComponentUtil.decodeColor("§cLe numéro de page introduit est incorrecte"));
                        return;
                    }

                    int max = count;
                    if (max > page * 10) {
                        max = page * 10;
                    }

                    String coloredColumns = "";
                    for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
                        coloredColumns += (coloredColumns.toString() != "" ? "§7, " : "") + (i % 2 == 0 ? "§a" : "§e") + result.getMetaData().getColumnName(i + 1);
                    }
                    // Affiche le résultat de la requete au joueur
                    sender.sendMessage(TextComponentUtil.decodeColor("§6Page numéro §f" + page + " §6sur §f" + ((int) Math.ceil(count / 10.0))));
                    sender.sendMessage(TextComponentUtil.decodeColor("§7-> " + coloredColumns));
                    sender.sendMessage(TextComponentUtil.decodeColor("§7"));

                    result.beforeFirst();
                    int i = 0;
                    while (result.next()) {
                        i++;
                        if (i > (page - 1) * 10 && i <= max) {
                            String message = "";
                            for (int j = 0; j < result.getMetaData().getColumnCount(); j++) {
                                message += (message != "" ? "§7, " : "") + (j % 2 == 0 ? "§a" : "§e") + result.getString(j + 1);
                            }
                            sender.sendMessage(TextComponentUtil.decodeColor("§7- §6" + message));
                        }
                    }
                }
                finally {
                    statement.close();
                    statement.getConnection().close();
                }
            }
            else if(arg0.equalsIgnoreCase("add") || arg0.equalsIgnoreCase("a")){
                // Check si le type est mit
                if(args.length < 2){
                    sender.sendMessage(TextComponentUtil.decodeColor("§6/database add §e<type>"));
                    return;
                }

                // Récuperer la class selon le nom de la table donné
                Class<?> classTable = null;
                for (Class<?> aClass : addable) {
                    if(aClass.getSimpleName().equalsIgnoreCase(arg1)){
                        classTable = aClass;
                    }
                }

                // Check si la class de table existe
                if(classTable == null){
                    sender.sendMessage(TextComponentUtil.decodeColor("§cLa table \"§f" + arg1 + "§c\" n'es pas trouvé !"));
                    String options = "";
                    for (Class<?> varClass : addable) {
                        options += (!options.toString().equals("") ? "§c, §f" : "§f") + varClass.getSimpleName().toLowerCase();
                    }
                    sender.sendMessage(TextComponentUtil.decodeColor("§cChoisisez entre: " + options));
                    return;
                }

                // Récupère la class entité de la table et les annotations
                val classEntity = classTable.getAnnotation(EntityRefference.class).entityClass();
                val anotation = classTable.getAnnotation(Addable.class);

                ArrayList<String> argsList = new ArrayList<>();
                for(int i = 2; i < args.length; i++){
                    String value = args[i];

                    // Vérifie si la valeur contien un guimet au cas ou il faut accepter les espaces
                    if(value.contains("\"")){
                        int quoteFound = 0;
                        String argsToEndOfQuote = "";
                        for(int j = i; j < args.length; j++){
                            argsToEndOfQuote += (!argsToEndOfQuote.toString().equals("") ? " " : "") + args[j];
                            if(args[j].contains("\"")){
                                quoteFound += args[j].length() - args[j].replaceAll("\"","").length();
                                i = j;
                                if(quoteFound == 2) break;
                            }
                        }
                        // Vérifie si il y a une fin
                        if(quoteFound < 2){
                            sender.sendMessage(TextComponentUtil.decodeColor("§cLa valeur " + argsToEndOfQuote + " doit au moins terminer par un guimet!"));
                            return;
                        }
                        else if(quoteFound > 2){
                            sender.sendMessage(TextComponentUtil.decodeColor("§cLa valeur " + argsToEndOfQuote + " contient trop de guimets!"));
                            return;
                        }

                        // Sépare entre le avant, dedans et après les guimets
                        // Example:
                        // avant"dedans"après
                        val split = argsToEndOfQuote.toString().split("\"");
                        if(split[0].length() > 0){
                            sender.sendMessage(TextComponentUtil.decodeColor("§cRien ne peut être mis avant le permier guimet (§f" + split[0] + "§c)"));
                            return;
                        }
                        if(split.length == 3 && split[2].length() > 0){
                            sender.sendMessage(TextComponentUtil.decodeColor("§cRien ne peut être mis après le deuxieme guimet (§f" + split[2] + "§c)"));
                            return;
                        }
                        value = split[1];
                    }
                    argsList.add(value);
                }

                // Check si le joueur a mis show ou filtre
                if(argsList.size() != anotation.defaultVariableName().length){
                    String variables = "";
                    for (String s : anotation.defaultVariableName()) {
                        variables += (!variables.toString().equals("") ? " " : "") + "<" + s + ">";
                    }
                    if(argsList.size() > anotation.defaultVariableName().length)
                        sender.sendMessage(TextComponentUtil.decodeColor("§cTrop d'arguments"));
                    else if(argsList.size() < anotation.defaultVariableName().length)
                        sender.sendMessage(TextComponentUtil.decodeColor("§cPas assez d'arguments"));
                    sender.sendMessage(TextComponentUtil.decodeColor("§6/database add " + arg1 + " " + variables));
                    return;
                }

                ArrayList<String> values = new ArrayList<>();
                int variable = 0;
                for(String arg: argsList){
                    Field field = getFieldFromClassWithSQLName(classEntity, anotation.defaultVariableName()[variable]);

                    if(field == null){
                        sender.sendMessage(TextComponentUtil.decodeColor("§cLa variable \"" + anotation.defaultVariableName()[variable] + "\" n'existe pas dans \"" + classEntity.getSimpleName() + "\" !"));
                        sender.sendMessage(TextComponentUtil.decodeColor(getFieldsStringFromClass(classEntity)));
                        return;
                    }

                    Attribute<?,?> attribute = (Attribute<?,?>)field.get(null);
                    if(attribute.getClassType().equals(Integer.class) || attribute.getClassType().equals(int.class)){
                        // Check si la valeur donné est un chiffre
                        if(!pattern.matcher(arg).matches()){
                            sender.sendMessage(TextComponentUtil.decodeColor("§c\"" + arg + "\" n'est pas un chiffre !"));
                            return;
                        }
                    }
                    values.add((arg.contains("'")? arg.replace("'", "\\'") : arg));
                    ++variable;
                }

                String query = "INSERT INTO " + ((Type<?>) classEntity.getField("$TYPE").get(null)).getName() + " ";

                String variablesSQL = "";
                for (String s : anotation.defaultVariableName()) {
                    variablesSQL += (!variablesSQL.toString().equals("") ? ", " : "") + "`" + s +"`";
                }

                String valuesSQL = "";
                for (String s : values) {
                    valuesSQL += (!valuesSQL.toString().equals("") ? ", " : "") + "'" + s +"'";
                }

                query += "(" + variablesSQL + ") VALUES (" + valuesSQL + ")";

                val statement = Main.getInstance().getHikari().getConnection().prepareStatement(query);
                try {
                    val count = statement.executeUpdate();

                    if (count == 0) {
                        sender.sendMessage(TextComponentUtil.decodeColor("§cAucune donnée n'a été ajouté à la base de données !"));
                        return;
                    }

                    sender.sendMessage(TextComponentUtil.decodeColor("§a" + count + " ajouts effectués !"));
                }
                finally {
                    statement.close();
                    statement.getConnection().close();
                }
            }
            else if(arg0.equalsIgnoreCase("remove") || arg0.equalsIgnoreCase("r")){

            }
            else if(arg0.equalsIgnoreCase("update") || arg0.equalsIgnoreCase("u")){
                // Check si le type est mit
                if(args.length < 2){
                    sender.sendMessage(TextComponentUtil.decodeColor("§6/database update §e<type>"));
                    return;
                }

                // Récuperer la class selon le nom de la table donné
                Class<?> classTable = null;
                for (Class<?> aClass : updatable) {
                    if(aClass.getSimpleName().equalsIgnoreCase(arg1)){
                        classTable = aClass;
                    }
                }

                // Check si la class de table existe
                if(classTable == null){
                    sender.sendMessage(TextComponentUtil.decodeColor("§cLa table \"§f" + arg1 + "§c\" n'es pas trouvé !"));
                    String options = "";
                    for (Class<?> varClass : updatable) {
                        options += (!options.toString().equals("") ? "§c, §f" : "§f") + varClass.getSimpleName().toLowerCase();
                    }
                    sender.sendMessage(TextComponentUtil.decodeColor("§cChoisisez entre: " + options));
                    return;
                }

                // Récupère la class entité de la table et les annotations
                val classEntity = classTable.getAnnotation(EntityRefference.class).entityClass();
                val anotation = classTable.getAnnotation(Updatable.class);


                if(args.length < 3){
                    String variables = "";
                    for (String s : anotation.defaultVariableName()) {
                        variables += (!variables.toString().equals("") ? " " : "") + "<" + s + ">";
                    }
                    sender.sendMessage(TextComponentUtil.decodeColor("§cChoisisez entre: " + variables));
                    return;
                }

                if(!Arrays.asList(anotation.defaultVariableName()).contains(args[2])){
                    String variables = "";
                    for (String s : anotation.defaultVariableName()) {
                        variables += (!variables.toString().equals("") ? " " : "") + "<" + s + ">";
                    }
                    sender.sendMessage(TextComponentUtil.decodeColor("§f" + args[2] + "§c n'est pas une varaible connu !"));
                    sender.sendMessage(TextComponentUtil.decodeColor("§cChoisisez entre: " + variables));
                    return;
                }

                if(args.length < 4){
                    sender.sendMessage(TextComponentUtil.decodeColor("§cVous devez entrer une valeur à changer !"));
                    sender.sendMessage(TextComponentUtil.decodeColor("§c/database " + args[0] + " " + args[1] + " " + args[2] + " <value> <condition>"));
                    return;
                }

                int valuePlace = 0;
                String value = "";
                for(int i = 3; i < args.length; i++){
                    value = args[i];

                    // Vérifie si la valeur contien un guimet au cas ou il faut accepter les espaces
                    if(value.contains("\"")){
                        int quoteFound = 0;
                        String argsToEndOfQuote = "";
                        for(int j = i; j < args.length; j++){
                            argsToEndOfQuote += (!argsToEndOfQuote.toString().equals("") ? " " : "") + args[j];
                            if(args[j].contains("\"")){
                                quoteFound += args[j].length() - args[j].replaceAll("\"","").length();
                                i = j;
                                if(quoteFound == 2) break;
                            }
                        }
                        // Vérifie si il y a une fin
                        if(quoteFound < 2){
                            sender.sendMessage(TextComponentUtil.decodeColor("§cLa valeur " + argsToEndOfQuote + " doit au moins terminer par un guimet!"));
                            return;
                        }
                        else if(quoteFound > 2){
                            sender.sendMessage(TextComponentUtil.decodeColor("§cLa valeur " + argsToEndOfQuote + " contient trop de guimets!"));
                            return;
                        }

                        // Sépare entre le avant, dedans et après les guimets
                        // Example:
                        // avant"dedans"après
                        val split = argsToEndOfQuote.toString().split("\"");
                        if(split[0].length() > 0){
                            sender.sendMessage(TextComponentUtil.decodeColor("§cRien ne peut être mis avant le permier guimet (§f" + split[0] + "§c)"));
                            return;
                        }
                        if(split.length == 3 && split[2].length() > 0){
                            sender.sendMessage(TextComponentUtil.decodeColor("§cRien ne peut être mis après le deuxieme guimet (§f" + split[2] + "§c)"));
                            return;
                        }
                        value = split[1];
                    }
                    valuePlace = i + 1;
                    break;
                }

                List<String> conditions = new ArrayList<>();
                // Boucle les argument autres
                for(int i = valuePlace; i < args.length; i++){

                    // TODO Faire de tous ce qu'il y a dans la boucle for une fonction pour réutiliser dans le update et delete
                    String[] conditionString = args[i].split("\\.");

                    // Check si il y a 3 argument à la condition
                    if(conditionString.length != 3){
                        sender.sendMessage(TextComponentUtil.decodeColor("§cLa condition \"" + args[i] + "\" est incorrecte !"));
                        return;
                    }

                    // Met la valeur de la condition
                    String valueCondition = conditionString[2];

                    // Vérifie si la valeur contien un guimet au cas ou il faut accepter les espaces
                    if(valueCondition.contains("\"")){
                        int quoteFound = 0;
                        String argsToEndOfQuote = "";
                        for(int j = i; j < args.length; j++){
                            argsToEndOfQuote += (!argsToEndOfQuote.toString().equals("") ? " " : "") + args[j];
                            if(args[j].contains("\"")){
                                quoteFound += args[j].length() - args[j].replaceAll("\"","").length();
                                i = j;
                                if(quoteFound == 2) break;
                            }
                        }
                        // Vérifie si il y a une fin
                        if(quoteFound < 2){
                            sender.sendMessage(TextComponentUtil.decodeColor("§cLa valeur de la condition doit au moins terminer par un guimet!"));
                            return;
                        }
                        else if(quoteFound > 2){
                            sender.sendMessage(TextComponentUtil.decodeColor("§cLa valeur de la condition contient trop de guimets!"));
                            return;
                        }

                        // Sépare entre le avant, dedans et après les guimets
                        // Example:
                        // avant"dedans"après
                        val split = argsToEndOfQuote.toString().split("\"");
                        val stringWithoutCondition = split[0].substring(conditionString[0].length() + conditionString[1].length() + 2);
                        if(stringWithoutCondition.length() > 0){
                            sender.sendMessage(TextComponentUtil.decodeColor("§cRien ne peut être mis avant le permier guimet (§f" + stringWithoutCondition + "§c)"));
                            return;
                        }
                        if(split.length == 3 && split[2].length() > 0){
                            sender.sendMessage(TextComponentUtil.decodeColor("§cRien ne peut être mis après le deuxieme guimet (§f" + split[2] + "§c)"));
                            return;
                        }
                        valueCondition = split[1];
                    }

                    String condition = null;
                    Field field = getFieldFromClass(classEntity, conditionString[0]);

                    if(field == null){
                        sender.sendMessage(TextComponentUtil.decodeColor("§cLa variable \"" + conditionString[0] + "\" n'existe pas dans \"" + classEntity.getSimpleName() + "\" !"));
                        sender.sendMessage(TextComponentUtil.decodeColor(getFieldsStringFromClass(classEntity)));
                        return;
                    }

                    Attribute<?,?> attribute = (Attribute<?,?>)field.get(null);
                    if(attribute.getClassType().equals(Integer.class) || attribute.getClassType().equals(int.class)){
                        // Check si le joueur a bien mis les bonnes conditions
                        if(!conditionString[1].equalsIgnoreCase("equal")
                                && !conditionString[1].equalsIgnoreCase("greater")
                                && !conditionString[1].equalsIgnoreCase("less"))
                        {
                            sender.sendMessage(TextComponentUtil.decodeColor("§cDes chiffres peuvent qu'avoir comme condition: equal, greater ou less"));
                            return;
                        }

                        // Check si la valeur donné est un chiffre
                        if(!pattern.matcher(valueCondition).matches()){
                            sender.sendMessage(TextComponentUtil.decodeColor("§c\"" + valueCondition + "\" n'est pas un chiffre !"));
                            return;
                        }

                        if(conditionString[1].equalsIgnoreCase("equal")){
                            condition = attribute.toString() + " = " + Integer.parseInt(valueCondition);
                            //condition = NamedExpression.ofInteger(attribute.toString()).eq(Integer.parseInt(value));
                        }
                        else if(conditionString[1].equalsIgnoreCase("greater")){
                            condition = attribute.toString() + " > " + Integer.parseInt(valueCondition);
                            //condition = NamedExpression.ofInteger(attribute.toString()).greaterThan(Integer.parseInt(value));
                        }
                        else if(conditionString[1].equalsIgnoreCase("less")){
                            condition = attribute.toString() + " < " + Integer.parseInt(valueCondition);
                            //condition = NamedExpression.ofInteger(attribute.toString()).lessThan(Integer.parseInt(value));
                        }
                    }
                    // Check si la variable est un text (String)
                    else if(attribute.getClassType().equals(String.class)){
                        // Check si le joueur a bien mis les bonnes conditions
                        if(!conditionString[1].equalsIgnoreCase("equal")
                                && !conditionString[1].equalsIgnoreCase("like"))
                        {
                            sender.sendMessage(TextComponentUtil.decodeColor("§cDu text ne peut qu'avoir comme condition: equal ou like"));
                            return;
                        }

                        if(conditionString[1].equalsIgnoreCase("equal")){
                            condition = attribute.toString() + " = '" + (valueCondition.contains("'")? valueCondition.replace("'", "\\'") : valueCondition) + "'";
                            //condition = NamedExpression.ofString(attribute.toString()).equalsIgnoreCase(value);
                        }
                        else if(conditionString[1].equalsIgnoreCase("like")){
                            String s = valueCondition.contains("'") ? valueCondition.replace("'", "\\'") : valueCondition;
                            condition = attribute.toString() + " LIKE '" + s + "'";
                            //condition = NamedExpression.ofString(attribute.toString()).like(value);
                        }
                    }
                    // Check si la variable est un text (String)
                    else if(attribute.getClassType().equals(Boolean.class) || attribute.getClassType().equals(boolean.class)){
                        // Check si le joueur a bien mis les bonnes conditions
                        if(!conditionString[1].equalsIgnoreCase("equal"))
                        {
                            sender.sendMessage(TextComponentUtil.decodeColor("§cUn boolean ne peut qu'avoir comme condition: equal"));
                            return;
                        }

                        if(conditionString[1].equalsIgnoreCase("equal")){
                            if(valueCondition.equalsIgnoreCase("true")){
                                condition = attribute.toString() + " = " + 1;
                            }
                            else if(valueCondition.equalsIgnoreCase("false")){
                                condition = attribute.toString() + " = " + 0;
                            }
                            else{
                                sender.sendMessage(TextComponentUtil.decodeColor("§c\"" + conditionString[0] + "\" est un boolean. Par conséquent il ne peut qu'être \"§ftrue§c\" ou \"§ffalse§c\""));
                                return;
                            }
                        }
                    }
                    // La variable est autre qu'un text ou un chiffre ou un boolean
                    else{
                        sender.sendMessage(TextComponentUtil.decodeColor("§c" + attribute.getClassType() + " n'est pas encore implémenté dans cette version !"));
                        return;
                    }



                    if(condition == null){
                        if(classEntity.getSimpleName().contains("Entity")){
                            sender.sendMessage(TextComponentUtil.decodeColor("§cVotre condition est incomplete!"));
                            sender.sendMessage(TextComponentUtil.decodeColor("§cVous devez préciser ce que vous voulez vérifier de §f" + conditionString[0]));
                            sender.sendMessage(TextComponentUtil.decodeColor("§cVoici les possibilités que vous pouvez récupérer:"));
                            sender.sendMessage(TextComponentUtil.decodeColor("§c" + getFieldsStringFromClass(classEntity)));
                        }
                    }
                    conditions.add(condition);
                }

                if(conditions.size() == 0){
                    sender.sendMessage(TextComponentUtil.decodeColor("§cPour l'opération d'update, vous devez mettre au moins une condition"));
                    return;
                }

                String query = "UPDATE " + ((Type<?>) classEntity.getField("$TYPE").get(null)).getName() + " SET " + args[2] + " = '" + value + "' WHERE " + conditions.get(0);

                if(conditions.size() > 1){
                    for(int i = 1; i < conditions.size(); i++){
                        query += " AND " + conditions.get(i);
                    }
                }

                val statement = Main.getInstance().getHikari().getConnection().prepareStatement(query);
                try{
                    val count = statement.executeUpdate();

                    if (count == 0) {
                        sender.sendMessage(TextComponentUtil.decodeColor("§cAucune donnée n'a été modifié dans la base de données !"));
                        return;
                    }

                    sender.sendMessage(TextComponentUtil.decodeColor("§a" + count + " modifications effectués !"));
                }
                finally {
                    statement.close();
                    statement.getConnection().close();
                }
            }
            else{
                sender.sendMessage(TextComponentUtil.decodeColor("§6/database §elist <type>"));
                sender.sendMessage(TextComponentUtil.decodeColor("§6/database §eadd <type>"));
                sender.sendMessage(TextComponentUtil.decodeColor("§6/database §eupdate <type>"));
                sender.sendMessage(TextComponentUtil.decodeColor("§6/database §eremove <type>"));
            }
        } catch (Exception e){
            e.printStackTrace();
            sender.sendMessage(TextComponentUtil.decodeColor("§cUne erreur est survenue.\n§f" + e.getMessage()));
        }
    }

    private String getFieldsStringFromClass(Class<?> clazz){
        String fields = "";
        for (Field fieldLoop : clazz.getFields()) {
            try {
                if(fieldLoop.get(null) instanceof Attribute<?,?>
                        && !(((Attribute<?,?>)fieldLoop.get(null)).getClassType().equals(MutableResult.class)
                            || ((Attribute<?,?>)fieldLoop.get(null)).getClassType().getSimpleName().contains("Entity"))){
                    fields += (fields != ""? "§6, §f": "§f") + fieldLoop.getName().toLowerCase();
                }
            } catch (IllegalAccessException ignored) {
            }
        }
        return fields;
    }

    private Field getFieldFromClass(Class<?> clazz, String fieldName){
        for (Field field : clazz.getFields()) {
            try {
                if(field.getName().equalsIgnoreCase(fieldName)
                        && field.get(null) != null && field.get(null) instanceof Attribute<?,?>
                        && !(((Attribute<?,?>)field.get(null)).getClassType().equals(MutableResult.class)
                            || ((Attribute<?,?>)field.get(null)).getClassType().getSimpleName().contains("Entity"))){
                    return field;
                }
            } catch (IllegalAccessException ignored) {
            }
        }
        return null;
    }

    private Field getFieldFromClassWithSQLName(Class<?> clazz, String sqlName){
        for (Field field : clazz.getFields()) {
            try {
                if(field.get(null) != null && field.get(null) instanceof Attribute<?,?>
                        && !(((Attribute<?,?>)field.get(null)).getClassType().equals(MutableResult.class)
                        || ((Attribute<?,?>)field.get(null)).getClassType().getSimpleName().contains("Entity"))
                        && ((Attribute<?,?>)field.get(null)).getName().equals(sqlName)){
                    return field;
                }
            } catch (IllegalAccessException ignored) {
            }
        }
        return null;
    }*/

    private static String argument(String[] args, int i) {
        if (args.length >= i) {
            return args[i - 1];
        }
        return "";
    }

    @SneakyThrows
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Main.getInstance().getPermissions().getDatabasePermission())) {
            sender.sendMessage(TextComponentUtil.decodeColor(Main.getInstance().getMessages().getNoPermission()));
            return;
        }

        String argsToString = "";
        for (String arg : args) {
            argsToString += " " + arg;
        }
        System.out.println(sender.getName() + " issued proxy command: /database" + argsToString);

        val arg0 = argument(args, 1);
        val arg1 = argument(args, 2);
        val arg2 = argument(args, 3);
        val arg3 = argument(args, 4);

        // Renvoie les erreurs à l'utilisateur de la commande
        try {
            // Vérifie si le premier argument n'est pas vide
            if (arg0.isEmpty()) {
                sender.sendMessage(TextComponentUtil.decodeColor("§6List of available tables: "));
                getTables().forEach(table -> sender.sendMessage(TextComponentUtil.decodeColor("§7 - §6" + table)));
                sender.sendMessage(TextComponentUtil.decodeColor("§6/database <table>"));
                return;
            }

            val listTables = getTables();

            // Vérifie si la table est donnée
            if (!ListUtils.containsIgnoreCase(listTables, arg0)) {
                sender.sendMessage(TextComponentUtil.decodeColor("§c" + arg0 + " doesn't exist in the tables list."));
                return;
            }
            val tableName = ListUtils.getIgnoreCase(listTables, arg0);

            // Si l'on veut reçevoir les données et les afficher
            if (arg1.equalsIgnoreCase("list") || arg1.equalsIgnoreCase("l")) {
                int page = 1;
                List<String> columns = new ArrayList<>();
                List<Quadlet<String, String, String, String>> joins = new ArrayList<>();
                List<Tuple<String, String>> whereConditions = new ArrayList<>();
                List<String> distincts = new ArrayList<>();

                // Boucle sur tous le prochains arguments après le "list"
                for (int i = 2; i < args.length; i++) {
                    // Vérifie si c'est une condition
                    if (args[i].contains(".")) {
                        String[] conditionString = args[i].split("\\.");

                        // Vérifier si la confition est bien construite
                        if (conditionString.length != 3) {
                            sender.sendMessage(TextComponentUtil.decodeColor("§cThe condition \"" + args[i] + "\" isn't correct !"));
                            sender.sendMessage(TextComponentUtil.decodeColor("§cThe condition can only be constituted of <condition>.<relational operator>.<value>"));
                            return;
                        }

                        // Met la valeur de la condition
                        String value = conditionString[2];

                        // Vérifie si la valeur contien un guimet au cas ou il faut accepter les espaces
                        if (value.contains("\"")) {
                            int quoteFound = 0;
                            String argsToEndOfQuote = "";
                            for (int j = i; j < args.length; j++) {
                                argsToEndOfQuote += (!argsToEndOfQuote.toString().equals("") ? " " : "") + args[j];
                                if (args[j].contains("\"")) {
                                    quoteFound += args[j].length() - args[j].replaceAll("\"", "").length();
                                    i = j;
                                    if (quoteFound == 2) break;
                                }
                            }
                            // Vérifie si il y a une fin
                            if (quoteFound < 2) {
                                sender.sendMessage(TextComponentUtil.decodeColor("§cThe value need to ends with a quotation mark (§f\"§c) !"));
                                return;
                            } else if (quoteFound > 2) {
                                sender.sendMessage(TextComponentUtil.decodeColor("§cThe value contains too much quotation marks (§f\"§c) !"));
                                return;
                            }

                            // Sépare entre le avant, dedans et après les guimets
                            // Example:
                            // avant"dedans"après
                            val split = argsToEndOfQuote.toString().split("\"");
                            val stringWithoutCondition = split[0].substring(conditionString[0].length() + conditionString[1].length() + 2);
                            if (stringWithoutCondition.length() > 0) {
                                sender.sendMessage(TextComponentUtil.decodeColor("§cNothing can be placed before the first quotation mark (§f" + stringWithoutCondition + "§c)"));
                                return;
                            }
                            if (split.length == 3 && split[2].length() > 0) {
                                sender.sendMessage(TextComponentUtil.decodeColor("§cNothing can be placed after the second quotation mark (§f" + split[2] + "§c)"));
                                return;
                            }
                            value = split[1];
                        }

                        val res = getColumAndTableFromText(sender, joins, tableName, conditionString[0]);
                        if(res == null) return;
                        joins = res.getFourth();
                        columns.add(res.getSecond() + "." + res.getThird());

                        System.out.println(res.getFirst() + " / " + res.getThird());

                        val columnName = ListUtils.getIgnoreCase(getColumns(res.getFirst()), res.getThird());
                        val columnInfo = getColumnInfo(res.getFirst(), res.getThird());

                        val relationalCondition = new HashMap<List<String>, String>();
                        relationalCondition.put(Arrays.asList("equals", "e", "="), "=");
                        relationalCondition.put(Arrays.asList("different", "d", "!="), "!=");

                        // Permet de filter selon le type du champ
                        if (columnInfo.get("DATA_TYPE").equalsIgnoreCase("int")) {
                            relationalCondition.put(Arrays.asList("greater", "g", ">"), ">");
                            relationalCondition.put(Arrays.asList("greaterequals", "ge", ">="), ">=");
                            relationalCondition.put(Arrays.asList("less", "l", "<"), "<");
                            relationalCondition.put(Arrays.asList("lessequals", "le", "<="), "<=");
                        } else if (columnInfo.get("DATA_TYPE").equalsIgnoreCase("varchar")) {
                            relationalCondition.put(Arrays.asList("like", "l"), "LIKE");
                        } else {
                            sender.sendMessage(TextComponentUtil.decodeColor("§f" + columnInfo.get("DATA_TYPE") + "§c ins't implemented yet in the conditions."));
                            return;
                        }

                        // Donne la valeur du filtre sous format sql
                        String relationalOperator = null;
                        for (val entry : relationalCondition.entrySet()) {
                            if (ListUtils.containsIgnoreCase(entry.getKey(), conditionString[1])) {
                                relationalOperator = entry.getValue();
                                break;
                            }
                        }

                        // Vérifie si la valeur du filtre n'est pas vide
                        if (relationalOperator == null) {
                            sender.sendMessage(TextComponentUtil.decodeColor("§cAn error occurs when searching the corresponding relational operator."));
                            sender.sendMessage(TextComponentUtil.decodeColor("§cChoose between those: "));
                            relationalCondition.keySet().forEach(list -> sender.sendMessage(TextComponentUtil.decodeColor("§7 - §c" + list.get(0))));
                            return;
                        }

                        // Ajout la condition à la requète
                        whereConditions.add(new Tuple(res.getSecond() + "." + columnName + " " + relationalOperator, value));
                    }
                    // Vérifie si l'argument est un chiffre
                    else if (pattern.matcher(args[i]).matches()) {
                        page = Integer.parseInt(args[i]);
                    }
                    // Le reste ira dans la spécification du champ lors de l'affichage
                    else {
                        /*if(args[i].contains("distinct-")){
                            val s = args[i].replace("distinct-", "");
                            val res = getColumAndTableFromText(sender, joins, tableName, s);
                            if(res == null) return;
                            joins = res.getFourth();
                            distincts.add(res.getSecond() + "." + res.getThird());
                        }
                        else {*/
                            for (String s : args[i].split(",")) {
                                val res = getColumAndTableFromText(sender, joins, tableName, s);
                                if(res == null) return;
                                joins = res.getFourth();
                                columns.add(res.getSecond() + "." + res.getThird());
                            }
                        /*}*/
                    }
                }

                // Si aucun champ n'est donné, il va trouver les 3 premiers champs de la table
                if (columns.isEmpty()) {
                    val columnsArray = getColumns(tableName);
                    for (int i = 0; i < 3 && i < columnsArray.size(); i++) {
                        columns.add(tableName + "." + columnsArray.get(i));
                    }
                }

                val queryParameters = new ArrayList<String>();
                String query = "SELECT $columns FROM $table$join$where";

                // Transforme les données de champs vers un format sql
                String columnQuery = "";
                for (String column : columns) {
                    columnQuery += (columnQuery != "" ? ", " : "") + column;
                }

                // Transforme les données de joins vers un format sql
                String joinQuery = "";
                for (Quadlet<String, String, String, String> join : joins) {
                    joinQuery += " JOIN " + join.getFirst() + " " + join.getSecond() + " ON(" + join.getThird() + " = " + join.getFourth() + ")";
                }

                // Transforme les données de conditions vers un format sql
                String whereQuery = "";
                for (Tuple<String, String> whereCondition : whereConditions) {
                    whereQuery += (whereQuery == "" ? " WHERE " : " AND ") + whereCondition.getLeft() + " ?";
                    queryParameters.add(whereCondition.getRight());
                }

                // Complete la requète sql
                query = query.replace("$columns", columnQuery)
                        .replace("$table", tableName)
                        .replace("$join", joinQuery)
                        .replace("$where", whereQuery);

                System.out.println(query);

                // Initialisation de la requète
                val con = Main.getInstance().getHikari().getConnection();
                val statement = con.prepareStatement(query);

                // Ajout des parameters à la requète
                int iParam = 1;
                for (String queryParameter : queryParameters) {
                    statement.setString(iParam, queryParameter);
                    iParam++;
                }

                val result = statement.executeQuery();

                result.last();
                int count = result.getRow();

                // Vérifie si une donnée est bien reçu
                if (count == 0) {
                    sender.sendMessage(TextComponentUtil.decodeColor("§cAucune donnée"));
                    return;
                }

                // Vérifie si la page est correcte
                if (page < 1 || page > ((int) Math.ceil(count / 10.0))) {
                    sender.sendMessage(TextComponentUtil.decodeColor("§cLe numéro de page introduit est incorrecte"));
                    return;
                }

                // Ajuste la maximimum selan la page demandée
                int max = count;
                if (max > page * 10) {
                    max = page * 10;
                }

                // Récupère les metadata de la table
                String coloredColumns = "";
                for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
                    coloredColumns += (coloredColumns.toString() != "" ? "§7, " : "") + (i % 2 == 0 ? "§a" : "§e") + result.getMetaData().getColumnName(i + 1);
                }
                // Affiche le résultat de la requete au joueur
                sender.sendMessage(TextComponentUtil.decodeColor("§6Page numéro §f" + page + " §6sur §f" + ((int) Math.ceil(count / 10.0))));
                sender.sendMessage(TextComponentUtil.decodeColor("§7-> " + coloredColumns));
                sender.sendMessage(TextComponentUtil.decodeColor("§7"));

                result.beforeFirst();
                int i = 0;
                while (result.next()) {
                    i++;
                    if (i > (page - 1) * 10 && i <= max) {
                        String message = "";
                        for (int j = 0; j < result.getMetaData().getColumnCount(); j++) {
                            message += (message != "" ? "§7, " : "") + (j % 2 == 0 ? "§a" : "§e") + result.getString(j + 1);
                        }
                        sender.sendMessage(TextComponentUtil.decodeColor("§7- §6" + message));
                    }
                }

                statement.close();
                con.close();

            } else if (arg1.equalsIgnoreCase("add") || arg1.equalsIgnoreCase("a")) {

            } else if (arg1.equalsIgnoreCase("update") || arg1.equalsIgnoreCase("u")) {

            } else if (arg1.equalsIgnoreCase("remove") || arg1.equalsIgnoreCase("r")) {

            } else if (arg1.equalsIgnoreCase("columns") || arg1.equalsIgnoreCase("c")) {
                sender.sendMessage(TextComponentUtil.decodeColor("§6List of columns in §f" + tableName + ":"));
                for (String column : getColumns(tableName)) {
                    sender.sendMessage(TextComponentUtil.decodeColor("§7 - §6" + column));
                }
            } else {
                sender.sendMessage(TextComponentUtil.decodeColor("§6The available options:"));
                sender.sendMessage(TextComponentUtil.decodeColor("§7/db " + tableName + "§6 list"));
                sender.sendMessage(TextComponentUtil.decodeColor("§7/db " + tableName + "§6 add"));
                sender.sendMessage(TextComponentUtil.decodeColor("§7/db " + tableName + "§6 update"));
                sender.sendMessage(TextComponentUtil.decodeColor("§7/db " + tableName + "§6 remove"));
                sender.sendMessage(TextComponentUtil.decodeColor("§7/db " + tableName + "§6 columns"));
            }
        } catch (Exception e) {
            sender.sendMessage(TextComponentUtil.decodeColor("§c" + e.getMessage()));
            e.printStackTrace();
        }
    }

    private List<String> getTables() throws SQLException {
        val con = Main.getInstance().getHikari().getConnection();
        val statement = con.prepareStatement("SHOW TABLES");

        val result = statement.executeQuery();
        val list = new ArrayList<String>();

        while (result.next()) {
            list.add(result.getString(1));
        }

        statement.close();
        con.close();

        return list;
    }

    private List<String> getColumns(String table) throws SQLException {
        val con = Main.getInstance().getHikari().getConnection();
        val statement = con.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? ORDER BY ORDINAL_POSITION ");
        statement.setString(1, table);

        val result = statement.executeQuery();

        val columns = new ArrayList<String>();

        while (result.next()) {
            columns.add(result.getString("COLUMN_NAME"));
        }

        statement.close();
        con.close();

        if (columns.isEmpty())
            return null;

        return columns;
    }

    private Map<String, String> getColumnInfo(String table, String column) throws SQLException {
        val con = Main.getInstance().getHikari().getConnection();
        val statement = con.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME = ? ORDER BY ORDINAL_POSITION ");
        statement.setString(1, table);
        statement.setString(2, column);

        val result = statement.executeQuery();
        val columnInfo = new HashMap<String, String>();

        if (result.next()) {
            for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
                val dataName = result.getMetaData().getColumnName(i + 1);
                columnInfo.put(dataName, result.getString(i + 1));
            }
        }

        statement.close();
        con.close();

        if (columnInfo.isEmpty())
            return null;

        return columnInfo;
    }

    private Tuple<String, String> getForeignKey(String table, String column) throws SQLException {
        val con = Main.getInstance().getHikari().getConnection();
        val statement = con.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME = ? AND COLUMN_NAME = ? AND REFERENCED_TABLE_NAME IS NOT NULL AND REFERENCED_COLUMN_NAME IS NOT NULL");
        statement.setString(1, table);
        statement.setString(2, column);

        val result = statement.executeQuery();
        final Tuple<String, String> pair;

        if (result.next())
            pair = new Tuple(result.getString("REFERENCED_TABLE_NAME"), result.getString("REFERENCED_COLUMN_NAME"));
        else
            pair = null;

        statement.close();
        con.close();

        return pair;
    }

    private Quadlet<String, String, String, List<Quadlet<String, String, String, String>>> getColumAndTableFromText(CommandSender sender, List<Quadlet<String, String, String, String>> joins, String tableName, String string) throws SQLException {
        String tempTable = tableName;
        String tempAliasTable = tableName;
        String tempColumn = string;

        if(string.contains(">")) {
            val temp = string.split(">");
            for (int c = 0; c < temp.length; c++) {
                tempColumn = temp[c];

                // Vérifie si le champ exist dans la bdd
                if (!ListUtils.containsIgnoreCase(getColumns(tempTable), tempColumn)) {
                    sender.sendMessage(TextComponentUtil.decodeColor("§cThe column §f" + tempColumn + "§c doesn't exist in the table §f" + tempTable + "§c !"));
                    sender.sendMessage(TextComponentUtil.decodeColor("§cType §f/database " + tempTable + " columns §cto see the available columns."));
                    return null;
                }

                val columnForeignKey = getForeignKey(tempTable, tempColumn);

                if(c != temp.length - 1){
                    if(columnForeignKey == null){
                        sender.sendMessage(TextComponentUtil.decodeColor("§cThe column §f" + tempColumn + "§c doesn't have any foreign key in table §f" + tempTable + "§c !"));
                        return null;
                    }

                    int i = 0;
                    for (Quadlet<String, String, String, String> join : joins) {
                        if(join.getFirst().toLowerCase().contains(columnForeignKey.getLeft().toLowerCase())){
                            i++;
                        }
                    }
                    joins.add(new Quadlet(columnForeignKey.getLeft(), columnForeignKey.getLeft() + i,  tempTable+ "." + tempColumn, columnForeignKey.getLeft() + i + "." + columnForeignKey.getRight()));

                    tempTable = columnForeignKey.getLeft();
                    tempAliasTable = columnForeignKey.getLeft() + i;
                }
            }
        }
        else{
            // Vérifie si le champ exist dans la bdd
            if (!ListUtils.containsIgnoreCase(getColumns(tempTable), tempColumn)) {
                sender.sendMessage(TextComponentUtil.decodeColor("§cThe column §f" + tempColumn + "§c doesn't exist in the table §f" + tempTable + "§c !"));
                sender.sendMessage(TextComponentUtil.decodeColor("§cType §f/database " + tempTable + " columns §cto see the available columns."));
                return null;
            }
        }
        return new Quadlet(tempTable, tempAliasTable, tempColumn, joins);
    }
}
