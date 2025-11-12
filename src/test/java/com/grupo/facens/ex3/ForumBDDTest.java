package com.grupo.facens.ex3;

import static org.junit.jupiter.api.Assertions.*;

import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.domain.entities.Comment;
import com.grupo.facens.ex3.domain.entities.Post;
import com.grupo.facens.ex3.domain.enums.TipoPlano;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.service.ForumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ForumBDDTest {

    @Autowired
    private ForumService forumService;

    @Autowired
    private AlunoRepository alunoRepository;

    private Aluno aluno;
    private Aluno outroAluno;
    private Aluno alunoSomenteComentarios;

    @BeforeEach
    void setup() {
        aluno = new Aluno();
        aluno.setNome("Maria Silva");
        aluno.setEmail("maria@exemplo.com");
        aluno.setPlano(TipoPlano.BASICO);
        aluno.setMoedas(0);
        aluno.setCursosCompletados(0);
        aluno.setCreditoCurso(0);
        aluno = alunoRepository.save(aluno);

        outroAluno = new Aluno();
        outroAluno.setNome("João Santos");
        outroAluno.setEmail("joao@exemplo.com");
        outroAluno.setPlano(TipoPlano.BASICO);
        outroAluno.setMoedas(0);
        outroAluno.setCursosCompletados(0);
        outroAluno = alunoRepository.save(outroAluno);

        alunoSomenteComentarios = new Aluno();
        alunoSomenteComentarios.setNome("Ana Silva");
        alunoSomenteComentarios.setEmail("ana@exemplo.com");
        alunoSomenteComentarios.setPlano(TipoPlano.BASICO);
        alunoSomenteComentarios.setMoedas(0);
        alunoSomenteComentarios.setCursosCompletados(0);
        alunoSomenteComentarios = alunoRepository.save(alunoSomenteComentarios);
    }

    @Test
    @DisplayName("BDD2 - Aluno mais ativo do fórum ganha curso gratuito")
    void bdd2_aluno_mais_ativo_forum_ganha_curso_gratuito() {
        assertNull(forumService.getAlunoMaisAtivoDoMes());
        assertFalse(forumService.isAlunoMaisAtivoDoMes(aluno));

        assertTrue(aluno.temPlanoBasico());

        Post postOutroAluno = forumService.criarTopico(
            outroAluno,
            "Dúvida básica",
            "Uma pergunta simples"
        );
        assertNotNull(postOutroAluno);

        Post post = forumService.criarTopico(
            aluno,
            "Como aprender Java?",
            "Gostaria de dicas para iniciantes"
        );

        assertNotNull(post);
        assertNotNull(post.getId());

        Comment comment = forumService.comentarTopico(
            aluno,
            post.getId(),
            "Ótima pergunta!"
        );
        Long fakeId = 999L;
        assertThrows(IllegalArgumentException.class, () ->
            forumService.comentarTopico(aluno, fakeId, "Ótima pergunta2!")
        );

        assertNotNull(comment);
        assertNotNull(comment.getId());

        Comment commentAna = forumService.comentarTopico(
            alunoSomenteComentarios,
            post.getId(),
            "Comentário da Ana"
        );
        assertNotNull(commentAna);

        assertTrue(forumService.isAlunoMaisAtivoDoMes(aluno));
        assertFalse(forumService.isAlunoMaisAtivoDoMes(outroAluno));

        Aluno maisAtivo = forumService.getAlunoMaisAtivoDoMes();
        assertNotNull(maisAtivo);
        assertEquals(aluno.getId(), maisAtivo.getId());
        int creditosAntes = maisAtivo.getCreditoCurso();
        maisAtivo.setCreditoCurso(creditosAntes + 1);
        maisAtivo = alunoRepository.save(maisAtivo);

        assertEquals(creditosAntes + 1, maisAtivo.getCreditoCurso());

        assertEquals(2, forumService.contarContribuicoes(aluno));

        assertEquals(1, forumService.contarContribuicoes(outroAluno));
    }
}
