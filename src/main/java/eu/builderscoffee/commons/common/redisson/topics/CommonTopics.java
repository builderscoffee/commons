package eu.builderscoffee.commons.common.redisson.topics;

import eu.builderscoffee.api.common.redisson.RedisTopic;

public class CommonTopics {

    public static RedisTopic STAFFCHAT = new RedisTopic("staffchat", "Chat privée pour le staff");
    public static RedisTopic SERVER_MANAGER = new RedisTopic("server_manager", "");
}
