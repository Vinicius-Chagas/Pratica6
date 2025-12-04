package com.grupo.facens.ex3.infrastructure.ai;

import static org.assertj.core.api.Assertions.assertThat;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "openrouter.api.key=test-key-12345"
})
class LangChainConfigTest {

    @Autowired
    private ChatLanguageModel chatLanguageModel;

    @Test
    void deveCriarChatLanguageModel() {
        assertThat(chatLanguageModel).isNotNull();
    }
}
