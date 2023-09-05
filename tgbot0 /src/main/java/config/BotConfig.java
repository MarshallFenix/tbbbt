package config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
// Аннотацию Value нужно добавлять не с ломбока, а со спринга!!!!
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {
    @Value("${bot.name}")
    String botName;
    @Value("${bot.token}")
    String token;
    @Value("${historyFile}")
    String historyPath;
}