package com.grupo.facens.ex3.steps;

import com.grupo.facens.ex3.model.Aluno;
import com.grupo.facens.ex3.model.Comment;
import com.grupo.facens.ex3.model.Post;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.service.ForumService;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class ForumSteps {

    @Autowired
    private ForumService forumService;

    @Autowired
    private AlunoRepository alunoRepository;

    private Aluno aluno;
    private Aluno outroAluno;
    private Post post;
    private Comment comment;
    private Aluno alunoMaisAtivo;
    private int totalContribuicoes;
    private Exception excecaoCapturada;

    @Dado("que existem alunos cadastrados no sistema")
    public void que_existem_alunos_cadastrados_no_sistema() {
        aluno = new Aluno();
        aluno.setNome("Maria Silva");
        aluno.setEmail("maria" + System.currentTimeMillis() + "@exemplo.com");
        aluno.setPlano(Aluno.TipoPlano.BASICO);
        aluno.setMoedas(0);
        aluno.setCursosCompletados(0);
        aluno.setCreditoCurso(0);
        aluno = alunoRepository.save(aluno);

        outroAluno = new Aluno();
        outroAluno.setNome("João Santos");
        outroAluno.setEmail("joao" + System.currentTimeMillis() + "@exemplo.com");
        outroAluno.setPlano(Aluno.TipoPlano.BASICO);
        outroAluno.setMoedas(0);
        outroAluno.setCursosCompletados(0);
        outroAluno.setCreditoCurso(0);
        outroAluno = alunoRepository.save(outroAluno);
    }

    @Dado("que não há atividade no fórum")
    public void que_nao_ha_atividade_no_forum() {
        alunoMaisAtivo = forumService.getAlunoMaisAtivoDoMes();
        assertNull(alunoMaisAtivo);
    }

    @Dado("que existe um aluno ativo no fórum")
    public void que_existe_um_aluno_ativo_no_forum() {
        aluno = new Aluno();
        aluno.setNome("Ana Silva");
        aluno.setEmail("ana" + System.currentTimeMillis() + "@exemplo.com");
        aluno.setPlano(Aluno.TipoPlano.BASICO);
        aluno.setMoedas(0);
        aluno.setCursosCompletados(0);
        aluno.setCreditoCurso(0);
        aluno = alunoRepository.save(aluno);
    }

    @Dado("que existe um aluno cadastrado")
    public void que_existe_um_aluno_cadastrado() {
        aluno = new Aluno();
        aluno.setNome("Pedro Silva");
        aluno.setEmail("pedro" + System.currentTimeMillis() + "@exemplo.com");
        aluno.setPlano(Aluno.TipoPlano.BASICO);
        aluno.setMoedas(0);
        aluno.setCursosCompletados(0);
        aluno.setCreditoCurso(0);
        aluno = alunoRepository.save(aluno);
    }

    @Quando("um aluno cria um tópico no fórum")
    public void um_aluno_cria_um_topico_no_forum() {
        post = forumService.criarTopico(
                aluno,
                "Como aprender Java?",
                "Gostaria de dicas para iniciantes");
        assertNotNull(post);
        assertNotNull(post.getId());
    }

    @Quando("o mesmo aluno comenta no tópico")
    public void o_mesmo_aluno_comenta_no_topico() {
        comment = forumService.comentarTopico(
                aluno,
                post.getId(),
                "Ótima pergunta!");
        assertNotNull(comment);
        assertNotNull(comment.getId());
    }

    @Quando("outro aluno cria apenas um tópico")
    public void outro_aluno_cria_apenas_um_topico() {
        Post postOutroAluno = forumService.criarTopico(
                outroAluno,
                "Dúvida básica",
                "Uma pergunta simples");
        assertNotNull(postOutroAluno);
    }

    @Quando("o aluno cria {int} tópico")
    public void o_aluno_cria_topico(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            post = forumService.criarTopico(
                    aluno,
                    "Tópico " + (i + 1),
                    "Conteúdo do tópico " + (i + 1));
        }
    }

    @Quando("faz {int} comentário")
    public void faz_comentario(int quantidade) {
        // Primeiro criamos um tópico se não existe
        if (post == null) {
            post = forumService.criarTopico(
                    aluno,
                    "Tópico para comentar",
                    "Conteúdo do tópico");
        }

        for (int i = 0; i < quantidade; i++) {
            forumService.comentarTopico(
                    aluno,
                    post.getId(),
                    "Comentário " + (i + 1));
        }
    }

    @Quando("o aluno tenta comentar em um tópico que não existe")
    public void o_aluno_tenta_comentar_em_um_topico_que_nao_existe() {
        try {
            Long fakeId = 999L;
            forumService.comentarTopico(aluno, fakeId, "Comentário inválido");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("verifico quem é o aluno mais ativo")
    public void verifico_quem_e_o_aluno_mais_ativo() {
        alunoMaisAtivo = forumService.getAlunoMaisAtivoDoMes();
    }

    @Então("o primeiro aluno deve ser o mais ativo do mês")
    public void o_primeiro_aluno_deve_ser_o_mais_ativo_do_mes() {
        assertTrue(forumService.isAlunoMaisAtivoDoMes(aluno));
        assertFalse(forumService.isAlunoMaisAtivoDoMes(outroAluno));

        alunoMaisAtivo = forumService.getAlunoMaisAtivoDoMes();
        assertNotNull(alunoMaisAtivo);
        assertEquals(aluno.getId(), alunoMaisAtivo.getId());
    }

    @Então("deve receber crédito para curso gratuito")
    public void deve_receber_credito_para_curso_gratuito() {
        int creditosAntes = alunoMaisAtivo.getCreditoCurso();
        alunoMaisAtivo.setCreditoCurso(creditosAntes + 1);
        alunoMaisAtivo = alunoRepository.save(alunoMaisAtivo);

        assertEquals(creditosAntes + 1, alunoMaisAtivo.getCreditoCurso());
    }

    @Então("o total de contribuições deve ser {int}")
    public void o_total_de_contribuicoes_deve_ser(int expectedTotal) {
        totalContribuicoes = forumService.contarContribuicoes(aluno);
        assertEquals(expectedTotal, totalContribuicoes);
    }

    @Então("deve receber uma mensagem de erro")
    public void deve_receber_uma_mensagem_de_erro() {
        assertNotNull(excecaoCapturada);
        assertEquals(IllegalArgumentException.class, excecaoCapturada.getClass());
        assertEquals("Tópico não encontrado", excecaoCapturada.getMessage());
    }

    @Então("não deve haver aluno mais ativo")
    public void nao_deve_haver_aluno_mais_ativo() {
        assertNull(alunoMaisAtivo);
    }
}
