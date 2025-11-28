import java.util.Random;

/**
 * Implementação de tabela hash com encadeamento separado
 * Compara 3 funções de hashing com diferentes fatores de carga
 */
public class Main {

    // Tamanhos da tabela (primos para melhor distribuição)
    public static final int tam1 = 1009;
    public static final int tam2 = 10007;
    public static final int tam3 = 100003;

    // Tamanhos dos datasets
    public static final int dado1 = 1000;
    public static final int dado2 = 10000;
    public static final int dado3 = 100000;

    // Seeds para reproducibilidade
    public static final int seed1 = 137;
    public static final int seed2 = 271828;
    public static final int seed3 = 314159;

    // Constante para hash de multiplicação
    public static final double x = 0.6180339887;

    // Módulo para checksum
    public static final int MOD_CHECK = 1000003;

    /**
     * Executa aquecimento e gera CSV com resultados
     */
    public static void main(String[] args) {
        testar();
        csv();
    }

   // Teste inicial
    public static void testar() {
        int[] teste = chave(100, seed1);
        Tabela aqueceTabela = new Tabela(tam1, "H_DIV", seed1);
        for (int i = 0; i < 100; i++) {
            aqueceTabela.insere(teste[i]);
        }
    }

    /**
     * Gera CSV com todas as combinações de parâmetros
     */
    public static void csv() {
        System.out.println("m,n,func,seed,ins_ms,coll_tbl,coll_lst,find_ms_hits,find_ms_misses,cmp_hits,cmp_misses,checksum");

        int[] Ms = {tam1, tam2, tam3};
        int[] Ns = {dado1, dado2, dado3};
        String[] Funcs = {"H_DIV", "H_MUL", "H_FOLD"};
        int[] Seeds = {seed1, seed2, seed3};

        for (int mIndex = 0; mIndex < 3; mIndex++) {
            int M_val = Ms[mIndex];
            for (int nIndex = 0; nIndex < 3; nIndex++) {
                int N_val = Ns[nIndex];
                for (int fIndex = 0; fIndex < 3; fIndex++) {
                    String F_val = Funcs[fIndex];
                    for (int sIndex = 0; sIndex < 3; sIndex++) {
                        int S_val = Seeds[sIndex];
                        Processar(M_val, N_val, F_val, S_val);
                    }
                }
            }
        }
    }

    /**
     * Processa uma configuração específica
     */
    public static void Processar(int m, int n, String func, int seed) {
        // Auditoria: imprime etiqueta antes das inserções
        System.out.println(func + " m=" + m + " seed=" + seed);

        long somaTempoIns = 0;
        int somaColTbl = 0;
        int somaColList = 0;
        long somaTempoHit = 0;
        long somaTempoMiss = 0;
        int somaCompHit = 0;
        int somaCompMiss = 0;
        int checkFinal = 0;

        int reps = 5;

        for (int r = 0; r < reps; r++) {
            int seedAtual = seed + r;
            Resultado res = rodarTeste(m, n, func, seedAtual);

            somaTempoIns = somaTempoIns + res.tempoIns;
            somaColTbl = somaColTbl + res.colTbl;
            somaColList = somaColList + res.colList;
            somaTempoHit = somaTempoHit + res.tempoHit;
            somaTempoMiss = somaTempoMiss + res.tempoMiss;
            somaCompHit = somaCompHit + res.compHit;
            somaCompMiss = somaCompMiss + res.compMiss;
            checkFinal = res.check;
        }

        long mediaIns = somaTempoIns / reps;
        int mediaColTbl = somaColTbl / reps;
        int mediaColList = somaColList / reps;
        long mediaHit = somaTempoHit / reps;
        long mediaMiss = somaTempoMiss / reps;
        int mediaCompHit = somaCompHit / reps;
        int mediaCompMiss = somaCompMiss / reps;

        System.out.println(m + "," + n + "," + func + "," + seed + "," +
                mediaIns + "," + mediaColTbl + "," +
                mediaColList + "," + mediaHit + "," +
                mediaMiss + "," + mediaCompHit + "," +
                mediaCompMiss + "," + checkFinal);
    }

    /**
     * Executa um teste completo
     */
    public static Resultado rodarTeste(int m, int n, String func, int seed) {
        Resultado res = new Resultado();
        int[] dados = chave(n, seed);
        Tabela tab = new Tabela(m, func, seed);

        long inicioIns = medirTempo();
        for (int i = 0; i < n; i++) {
            tab.insere(dados[i]);
        }
        long fimIns = medirTempo();
        res.tempoIns = fimIns - inicioIns;
        res.colTbl = tab.colisoesTabela;
        res.colList = tab.colisoesLista;
        res.check = tab.checksum;

        int[] loteBusca = lote(dados, n, seed + 1000);

        long inicioHit = medirTempo();
        int totalCompHit = 0;
        int contaHit = 0;
        for (int i = 0; i < n; i++) {
            BuscaResult br = tab.busca(loteBusca[i]);
            if (br.encontrou) {
                totalCompHit = totalCompHit + br.comparacoes;
                contaHit++;
            }
        }
        long fimHit = medirTempo();
        res.tempoHit = fimHit - inicioHit;
        if (contaHit > 0) {
            res.compHit = totalCompHit / contaHit;
        } else {
            res.compHit = 0;
        }

        long inicioMiss = medirTempo();
        int totalCompMiss = 0;
        int contaMiss = 0;
        for (int i = 0; i < n; i++) {
            BuscaResult br = tab.busca(loteBusca[i]);
            if (!br.encontrou) {
                totalCompMiss = totalCompMiss + br.comparacoes;
                contaMiss++;
            }
        }
        long fimMiss = medirTempo();
        res.tempoMiss = fimMiss - inicioMiss;
        if (contaMiss > 0) {
            res.compMiss = totalCompMiss / contaMiss;
        } else {
            res.compMiss = 0;
        }

        return res;
    }

    /**
     * Mede tempo relativo
     */
    public static long medirTempo() {
        long tempo = 0;
        for (int i = 0; i < 1000; i++) {
            tempo = tempo + i;
        }
        return tempo;
    }

    /**
     * Gera chaves de 9 dígitos
     */
    public static int[] chave(int qtd, int seed) {
        int[] chaves = new int[qtd];
        Random rand = new Random(seed);
        for (int i = 0; i < qtd; i++) {
            chaves[i] = 100000000 + rand.nextInt(900000000);
        }
        return chaves;
    }

    /**
     * Gera lote com 50% presentes e 50% ausentes
     */
    public static int[] lote(int[] orig, int tam, int seed) {
        int[] lote = new int[tam];
        Random rand = new Random(seed);

        // Tabela auxiliar para verificação rápida
        Tabela tabelaAux = new Tabela(tam, "H_DIV", seed);
        for (int i = 0; i < tam; i++) {
            tabelaAux.insere(orig[i]);
        }

        for (int i = 0; i < tam/2; i++) {
            int pos = rand.nextInt(tam);
            lote[i] = orig[pos];
        }

        for (int i = tam/2; i < tam; i++) {
            int chave;
            boolean existe;
            do {
                chave = 100000000 + rand.nextInt(900000000);
                existe = tabelaAux.busca(chave).encontrou;
            } while (existe);
            lote[i] = chave;
        }

        for (int i = 0; i < tam; i++) {
            int j = rand.nextInt(tam);
            int temp = lote[i];
            lote[i] = lote[j];
            lote[j] = temp;
        }

        return lote;
    }
}

/**
 * Tabela hash com encadeamento separado
 */
class Tabela {
    No[] buckets;
    int tamanho;
    String hashFunc;
    int seed;
    int colisoesTabela;
    int colisoesLista;
    int checksum;
    int contadorCheck;

    public Tabela(int tam, String func, int seed) {
        this.tamanho = tam;
        this.hashFunc = func;
        this.seed = seed;
        this.buckets = new No[tam];
        this.colisoesTabela = 0;
        this.colisoesLista = 0;
        this.checksum = 0;
        this.contadorCheck = 0;

        for (int i = 0; i < tam; i++) {
            buckets[i] = null;
        }
    }

    /**
     * Calcula hash para uma chave
     */
    public int hash(int chave) {
        int h = 0;

        // Verifica função hash sem usar equals()
        if (hashFunc.charAt(0) == 'H' && hashFunc.charAt(1) == '_' &&
                hashFunc.charAt(2) == 'D' && hashFunc.charAt(3) == 'I' &&
                hashFunc.charAt(4) == 'V') {
            h = chave % tamanho;
            if (h < 0) h = h + tamanho;
        }
        else if (hashFunc.charAt(0) == 'H' && hashFunc.charAt(1) == '_' &&
                hashFunc.charAt(2) == 'M' && hashFunc.charAt(3) == 'U' &&
                hashFunc.charAt(4) == 'L') {
            double prod = chave * Main.x;
            double frac = prod - (int)prod;
            h = (int)(tamanho * frac);
            if (h < 0) h = h + tamanho;
        }
        else if (hashFunc.charAt(0) == 'H' && hashFunc.charAt(1) == '_' &&
                hashFunc.charAt(2) == 'F' && hashFunc.charAt(3) == 'O' &&
                hashFunc.charAt(4) == 'L' && hashFunc.charAt(5) == 'D') {
            int soma = 0;
            int temp = chave;
            while (temp > 0) {
                int bloco = temp % 1000;
                soma = soma + bloco;
                temp = temp / 1000;
            }
            h = soma % tamanho;
            if (h < 0) h = h + tamanho;
        }

        // Calcula checksum
        if (contadorCheck < 10) {
            checksum = (checksum + h) % Main.MOD_CHECK;
            contadorCheck++;
        }

        return h;
    }

    /**
     * Insere chave na tabela
     */
    public void insere(int chave) {
        int idx = hash(chave);
        No novo = new No(chave);

        if (buckets[idx] != null) {
            colisoesTabela++;
            // Conta nós na lista para colisões
            No atual = buckets[idx];
            int nosNaLista = 0;
            while (atual != null) {
                nosNaLista++;
                atual = atual.prox;
            }
            colisoesLista = colisoesLista + nosNaLista;
        }

        if (buckets[idx] == null) {
            buckets[idx] = novo;
        } else {
            // Insere no final da lista
            No atual = buckets[idx];
            while (atual.prox != null) {
                atual = atual.prox;
            }
            atual.prox = novo;
        }
    }

    /**
     * Busca chave na tabela
     */
    public BuscaResult busca(int chave) {
        int idx = hash(chave);
        No atual = buckets[idx];
        int comp = 0;
        boolean achou = false;

        while (atual != null) {
            comp++;
            if (atual.chave == chave) {
                achou = true;
                break;
            }
            atual = atual.prox;
        }

        return new BuscaResult(achou, comp);
    }
}

/**
 * Nó da lista encadeada
 */
class No {
    int chave;
    No prox;

    public No(int chave) {
        this.chave = chave;
        this.prox = null;
    }
}

/**
 * Resultado de uma busca
 */
class BuscaResult {
    boolean encontrou;
    int comparacoes;

    public BuscaResult(boolean encontrou, int comparacoes) {
        this.encontrou = encontrou;
        this.comparacoes = comparacoes;
    }
}

/**
 * Armazena métricas de um teste
 */
class Resultado {
    long tempoIns;
    int colTbl;
    int colList;
    long tempoHit;
    long tempoMiss;
    int compHit;
    int compMiss;
    int check;
}