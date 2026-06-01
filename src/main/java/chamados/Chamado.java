package chamados;

/**
 * Representa um chamado no sistema de atendimento.
 * Possui prioridade (menor valor = maior prioridade) e descrição.
 * O timestamp é usado para desempate: chamados mais antigos têm precedência.
 */
public class Chamado {

    private int prioridade;
    private String descricao;
    private final long timestamp; // momento de criação, usado para desempate

    public Chamado(int prioridade, String descricao) {
        this.prioridade = prioridade;
        this.descricao = descricao;
        this.timestamp = System.nanoTime(); // nanosegundos para precisão
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[Prioridade %d] %s", prioridade, descricao);
    }
}
