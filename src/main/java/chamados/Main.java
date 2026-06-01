package chamados;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Aplicação de console para o Sistema de Chamados com Fila de Prioridade.
 *
 * Menu de operações:
 *   1 - Adicionar chamado
 *   2 - Atender próximo chamado (dequeue)
 *   3 - Consultar próximo sem atender (peek)
 *   4 - Exibir todos os chamados pendentes
 *   5 - Ver quantidade de chamados
 *   0 - Sair
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final SistemaDeChamado sistema = new SistemaDeChamado();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   SISTEMA DE CHAMADOS — FILA DE PRIORIDADE");
        System.out.println("========================================");

        int opcao = -1;
        while (opcao != 0) {
            exibirMenu();
            opcao = lerInt("Opção: ");
            System.out.println();

            switch (opcao) {
                case 1 -> adicionarChamado();
                case 2 -> atenderProximo();
                case 3 -> consultarProximo();
                case 4 -> exibirPendentes();
                case 5 -> exibirQuantidade();
                case 0 -> System.out.println("Encerrando o sistema. Até logo!");
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
            System.out.println();
        }
    }

    // ------------------------------------------------------------------ //

    private static void exibirMenu() {
        System.out.println("----------------------------------------");
        System.out.println(" 1. Adicionar chamado");
        System.out.println(" 2. Atender próximo chamado");
        System.out.println(" 3. Consultar próximo (sem atender)");
        System.out.println(" 4. Exibir chamados pendentes");
        System.out.println(" 5. Quantidade de chamados na fila");
        System.out.println(" 0. Sair");
        System.out.println("----------------------------------------");
    }

    private static void adicionarChamado() {
        System.out.println("--- Novo Chamado ---");
        int prioridade = lerInt("Prioridade (inteiro, menor = mais urgente): ");
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine().trim();

        Chamado chamado = new Chamado(prioridade, descricao);
        sistema.adicionar(chamado);

        System.out.printf("✔ Chamado adicionado com sucesso! (%s)%n", chamado);
    }

    private static void atenderProximo() {
        try {
            Chamado atendido = sistema.proximo();
            System.out.println("✔ Chamado atendido: " + atendido);
            System.out.printf("  Chamados restantes na fila: %d%n", sistema.tamanho());
        } catch (IllegalStateException e) {
            System.out.println("✘ " + e.getMessage());
        }
    }

    private static void consultarProximo() {
        try {
            Chamado proximo = sistema.consultarOProximo();
            System.out.println("Próximo a ser atendido: " + proximo);
        } catch (IllegalStateException e) {
            System.out.println("✘ " + e.getMessage());
        }
    }

    private static void exibirPendentes() {
        System.out.printf("Chamados pendentes (%d):%n", sistema.tamanho());
        sistema.exibirChamadosPendentes();
    }

    private static void exibirQuantidade() {
        System.out.printf("Total de chamados na fila: %d%n", sistema.tamanho());
    }

    // ------------------------------------------------------------------ //

    /**
     * Lê um inteiro do console, repetindo em caso de entrada inválida.
     */
    private static int lerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int valor = scanner.nextInt();
                scanner.nextLine(); // consumir quebra de linha
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // descartar entrada inválida
                System.out.println("Entrada inválida. Digite um número inteiro.");
            }
        }
    }
}
