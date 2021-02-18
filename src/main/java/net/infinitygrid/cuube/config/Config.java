package net.infinitygrid.cuube.config;

@SuppressWarnings("ConstantConditions")
public class Config {

    private final String botToken = null;
    private ConfigChannels channels;
    private ConfigMessage messages;

    public Config() {
        channels = new ConfigChannels();
        messages = new ConfigMessage();
    }

    public String getBotToken() {
        return botToken;
    }

    public ConfigMessage getMessages() {
        return messages;
    }

    public ConfigChannels getChannels() {
        return channels;
    }

}
