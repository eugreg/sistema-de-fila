package chamados;

/**
 * Fila de prioridade implementada manualmente com array dinâmico (heap binário mínimo).
 *
 * Regras de prioridade:
 *   - Menor valor de prioridade = atendido primeiro (ex: prioridade 1 > prioridade 3)
 *   - Desempate: chamado mais antigo (menor timestamp) vem primeiro
 *
 * Complexidade:
 *   - adicionar():        O(log n) — sobe pelo heap (sift up)
 *   - proximo():          O(log n) — desce pelo heap (sift down)
 *   - consultarOProximo(): O(1)    — apenas lê a raiz
 *   - tamanho():          O(1)
 */
public class SistemaDeChamado {

    private static final int CAPACIDADE_INICIAL = 16;

    private Chamado[] heap;
    private int tamanho;

    public SistemaDeChamado() {
        heap = new Chamado[CAPACIDADE_INICIAL];
        tamanho = 0;
    }

    // ------------------------------------------------------------------ //
    //  Interface pública                                                   //
    // ------------------------------------------------------------------ //

    /**
     * Enfileira um chamado respeitando a prioridade.
     * Complexidade: O(log n)
     */
    public void adicionar(Chamado chamado) {
        garantirCapacidade();
        heap[tamanho] = chamado;
        siftUp(tamanho);
        tamanho++;
    }

    /**
     * Remove e retorna o chamado de maior prioridade (menor valor).
     * Em caso de empate, retorna o chamado mais antigo.
     * Complexidade: O(log n)
     *
     * @throws IllegalStateException se a fila estiver vazia
     */
    public Chamado proximo() {
        validarNaoVazia();
        Chamado primeiro = heap[0];
        tamanho--;
        if (tamanho > 0) {
            heap[0] = heap[tamanho];
            heap[tamanho] = null; // ajuda o GC
            siftDown(0);
        } else {
            heap[0] = null;
        }
        return primeiro;
    }

    /**
     * Consulta (sem remover) o próximo chamado a ser atendido.
     * Complexidade: O(1)
     *
     * @throws IllegalStateException se a fila estiver vazia
     */
    public Chamado consultarOProximo() {
        validarNaoVazia();
        return heap[0];
    }

    /**
     * Retorna a quantidade de chamados aguardando atendimento.
     * Complexidade: O(1)
     */
    public long tamanho() {
        return tamanho;
    }

    // ------------------------------------------------------------------ //
    //  Operações internas do heap                                          //
    // ------------------------------------------------------------------ //

    /**
     * Sobe o elemento na posição {@code i} até sua posição correta no heap.
     */
    private void siftUp(int i) {
        while (i > 0) {
            int pai = (i - 1) / 2;
            if (temPrioridade(heap[i], heap[pai])) {
                trocar(i, pai);
                i = pai;
            } else {
                break;
            }
        }
    }

    /**
     * Desce o elemento na posição {@code i} até sua posição correta no heap.
     */
    private void siftDown(int i) {
        while (true) {
            int menor = i;
            int esq = 2 * i + 1;
            int dir = 2 * i + 2;

            if (esq < tamanho && temPrioridade(heap[esq], heap[menor])) {
                menor = esq;
            }
            if (dir < tamanho && temPrioridade(heap[dir], heap[menor])) {
                menor = dir;
            }

            if (menor == i) break;

            trocar(i, menor);
            i = menor;
        }
    }

    /**
     * Retorna {@code true} se {@code a} deve ser atendido antes de {@code b}.
     * Critério 1: menor valor de prioridade vem primeiro.
     * Critério 2 (desempate): timestamp menor (chegou antes) vem primeiro.
     */
    private boolean temPrioridade(Chamado a, Chamado b) {
        if (a.getPrioridade() != b.getPrioridade()) {
            return a.getPrioridade() < b.getPrioridade();
        }
        return a.getTimestamp() < b.getTimestamp();
    }

    private void trocar(int i, int j) {
        Chamado temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // ------------------------------------------------------------------ //
    //  Gerenciamento de capacidade                                         //
    // ------------------------------------------------------------------ //

    /**
     * Duplica o array interno quando necessário (amortizado O(1) por inserção).
     */
    private void garantirCapacidade() {
        if (tamanho == heap.length) {
            Chamado[] novoHeap = new Chamado[heap.length * 2];
            System.arraycopy(heap, 0, novoHeap, 0, heap.length);
            heap = novoHeap;
        }
    }

    // ------------------------------------------------------------------ //
    //  Utilitários                                                         //
    // ------------------------------------------------------------------ //

    private void validarNaoVazia() {
        if (tamanho == 0) {
            throw new IllegalStateException("Não há chamados na fila.");
        }
    }

    /**
     * Exibe todos os chamados aguardando atendimento (sem alterar a fila).
     * Ordem de exibição é a do array interno — não necessariamente a ordem de atendimento.
     */
    public void exibirChamadosPendentes() {
        if (tamanho == 0) {
            System.out.println("  (nenhum chamado pendente)");
            return;
        }
        for (int i = 0; i < tamanho; i++) {
            System.out.printf("  %d. %s%n", i + 1, heap[i]);
        }
    }
}
