package com.grupo.facens.ex3.infrastructure.ai;

import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.domain.entities.Curso;
import com.grupo.facens.ex3.domain.enums.Dificuldade;
import dev.langchain4j.model.chat.ChatLanguageModel;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TutorService {

    private final ChatLanguageModel model;

    public String sugerirProximosCursos(Aluno aluno, List<Curso> cursosDisponiveis) {
        String cursosInfo = cursosDisponiveis.stream()
                .limit(10)
                .map(c -> String.format("- %s (%s, %s)",
                        c.getTitulo(),
                        c.getCategoria(),
                        c.getDificuldade()))
                .collect(Collectors.joining("\n"));

        String prompt = String.format(
                "Você é um tutor de educação continuada especializado em recomendar cursos.\n" +
                        "O aluno se chama %s e está no plano %s.\n" +
                        "Ele já completou %d cursos e possui %d moedas.\n\n" +
                        "Cursos disponíveis:\n%s\n\n" +
                        "Com base no perfil do aluno e nos cursos disponíveis, sugira os próximos 3 cursos mais adequados "
                        +
                        "e explique brevemente o porquê de cada recomendação. " +
                        "Seja objetivo e focado no desenvolvimento do aluno.",
                aluno.getNome(),
                aluno.getPlano() != null ? aluno.getPlano().name() : "sem plano",
                aluno.getCursosCompletados(),
                aluno.getMoedas(),
                cursosInfo);

        return model.generate(prompt);
    }

    public String sugerirEstrategiasEstudo(Aluno aluno) {
        String prompt = String.format(
                "Você é um tutor de educação continuada.\n" +
                        "O aluno se chama %s, está no plano %s e já completou %d cursos.\n" +
                        "Ele possui %d moedas e %d créditos de curso.\n\n" +
                        "Sugira estratégias personalizadas de estudo em 4 tópicos:\n" +
                        "1. Organização do tempo de estudo\n" +
                        "2. Técnicas de aprendizado eficazes\n" +
                        "3. Como manter a motivação\n" +
                        "4. Próximos passos recomendados\n\n" +
                        "Adapte as sugestões ao perfil e progresso do aluno.",
                aluno.getNome(),
                aluno.getPlano() != null ? aluno.getPlano().name() : "sem plano",
                aluno.getCursosCompletados(),
                aluno.getMoedas(),
                aluno.getCreditoCurso());

        return model.generate(prompt);
    }

    public String sugerirCursosPorCategoria(Aluno aluno, String categoria, List<Curso> cursos) {
        String cursosInfo = cursos.stream()
                .filter(c -> c.getCategoria().equalsIgnoreCase(categoria))
                .limit(5)
                .map(c -> String.format("- %s (%s)", c.getTitulo(), c.getDificuldade()))
                .collect(Collectors.joining("\n"));

        String prompt = String.format(
                "Você é um tutor de educação continuada.\n" +
                        "O aluno %s está interessado em cursos da categoria '%s'.\n" +
                        "Ele está no plano %s e já completou %d cursos.\n\n" +
                        "Cursos disponíveis nesta categoria:\n%s\n\n" +
                        "Recomende os melhores cursos desta categoria para o aluno, " +
                        "considerando seu nível de experiência e plano atual. " +
                        "Explique por que cada curso é adequado.",
                aluno.getNome(),
                categoria,
                aluno.getPlano() != null ? aluno.getPlano().name() : "sem plano",
                aluno.getCursosCompletados(),
                cursosInfo.isEmpty() ? "Nenhum curso disponível nesta categoria." : cursosInfo);

        return model.generate(prompt);
    }

    public String sugerirCursosPorDificuldade(Aluno aluno, Dificuldade dificuldade, List<Curso> cursos) {
        String cursosInfo = cursos.stream()
                .filter(c -> c.getDificuldade() == dificuldade)
                .limit(5)
                .map(c -> String.format("- %s (%s)", c.getTitulo(), c.getCategoria()))
                .collect(Collectors.joining("\n"));

        String prompt = String.format(
                "Você é um tutor de educação continuada.\n" +
                        "O aluno %s quer cursos de dificuldade '%s'.\n" +
                        "Ele está no plano %s, já completou %d cursos e possui %d moedas.\n\n" +
                        "Cursos disponíveis com esta dificuldade:\n%s\n\n" +
                        "Recomende os melhores cursos desta dificuldade para o aluno, " +
                        "explicando como eles se alinham com seu progresso atual.",
                aluno.getNome(),
                dificuldade.name(),
                aluno.getPlano() != null ? aluno.getPlano().name() : "sem plano",
                aluno.getCursosCompletados(),
                aluno.getMoedas(),
                cursosInfo.isEmpty() ? "Nenhum curso disponível com esta dificuldade." : cursosInfo);

        return model.generate(prompt);
    }

    public String fornecerDicasPersonalizadas(Aluno aluno, String contexto) {
        String prompt = String.format(
                "Você é um tutor de educação continuada.\n" +
                        "O aluno %s está no plano %s, completou %d cursos e possui %d moedas.\n" +
                        "Contexto adicional: %s\n\n" +
                        "Forneça dicas personalizadas e práticas para ajudar o aluno a melhorar seu aprendizado. " +
                        "Seja específico e acionável.",
                aluno.getNome(),
                aluno.getPlano() != null ? aluno.getPlano().name() : "sem plano",
                aluno.getCursosCompletados(),
                aluno.getMoedas(),
                contexto);

        return model.generate(prompt);
    }
}
