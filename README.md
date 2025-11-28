# Relatório: Tabela Hash

**Disciplina:** Resolução de Problemas Estruturados em Computação   
**Aluno:** André Luís Scahaiber Alves   
**Professor:** Andrey Cabral Meira   
**Curso:** Ciência da Computação   
**Universidade:** Pontifícia Universidade Católica do Paraná (PUC-PR)   


Este repositório contém os resultados da implementação e análise experimental de uma Tabela Hash utilizando a estratégia de **Encadeamento Separado (Separate Chaining)** para resolução de colisões.

O objetivo principal é avaliar o impacto de diferentes funções de hashing e variações no fator de carga (`α = n/m`) sobre o desempenho e a distribuição das chaves.

**LINK PARA O YOUTUBE:**

## 1. Contexto do Projeto

| Tópico | Descrição | 
 | ----- | ----- | 
| **Objetivo** | Implementação em Java e análise experimental do custo médio de operações em uma Tabela Hash com encadeamento separado. | 
| **Estrutura** | Tabela hash baseada em `array` de listas encadeadas. As listas encadeadas foram implementadas manualmente (sem uso de classes avançadas de coleções do Java). | 
| **Foco** | Analisar o desempenho sob diferentes fatores de carga e comparar a eficácia de diversas funções de hashing na dispersão de chaves numéricas de 9 dígitos. | 

## 2. Metodologia Experimental

“Distribuições mais uniformes reduzem o custo médio no encadeamento separado.”
A robustez da análise baseia-se na execução de múltiplas repetições com `seeds` fixas para garantir a reprodutibilidade dos resultados.

### 2.1. Configuração de Chaves

| Parâmetro | Valores Utilizados | 
 | ----- | ----- | 
| **Tamanhos de Tabela (`m`)** | `{1009, 10007, 100003}` (Valores primos para otimizar H_DIV) | 
| **Conjuntos de Dados (`n`)** | `{1000, 10000, 100000}` chaves numéricas de 9 dígitos (geradas aleatoriamente) | 
| **Seeds Públicas** | `{137, 271828, 314159}` (Para geração de chaves e reprodutibilidade) | 
| **Repetições** | 5 execuções independentes por configuração | 

### 2.2. Funções Hash Avaliadas

Foram implementadas e comparadas três funções hash distintas:

1. **H_DIV (Método da Divisão):** `h(k) = k mod m`. Conhecido pela simplicidade, mas sensível à escolha de `m`.

2. **H_MUL (Método da Multiplicação):** Utiliza a constante áurea (`0.6180339887`) para tentar garantir uma distribuição mais uniforme, independentemente do tamanho `m`.

3. **H_FOLD (Método do Dobramento):** Particiona a chave em blocos (`3 dígitos`) e realiza uma soma/dobramento para gerar o índice.

### 2.3. Métricas Coletadas

As seguintes métricas foram coletadas para cada execução e serão analisadas no relatório:

| Métrica | Propósito | 
 | ----- | ----- | 
| **Tempo de Inserção** | Tempo total (ms) necessário para inserir todas as `n` chaves. | 
| **Colisões na Tabela** | Número de chaves que mapearam para um *slot* já ocupado (ou seja, criaram uma lista de tamanho > 1). | 
| **Colisões nas Listas** | Comprimento médio e máximo das listas encadeadas (indicador direto de congestionamento). | 
| **Tempo de Busca** | Tempo para buscas bem-sucedidas (`hits`) e malsucedidas (`misses`). | 
| **Comparações** | Número médio de comparações de chaves necessárias para buscas (`hits` e `misses`). | 
| **Checksum** | Usado para auditoria e validação da integridade dos dados inseridos. | 

## 3. Estrutura do Relatório

O relatório final está organizado para cobrir detalhadamente as seguintes áreas de análise:

1. **Análise do Fator de Carga (`α`):** O impacto de `α` no tempo e no número de comparações.

2. **Comparação das Funções Hash:** Desempenho relativo de H_DIV, H_MUL e H_FOLD.

3. **Desempenho em Operações de Busca:** Análise detalhada dos custos (comparações e tempo) para `hits` e `misses`.

4. **Análise de Colisões:** Frequência de colisões e o comprimento resultante das listas encadeadas.

## Anexo e Revisão

Para uma revisão completa, os dados brutos estão disponíveis em:

* **SHEETS: Dados Brutos:** Contém um exemplo do arquivo CSV gerado com as métricas coletadas em todas as 5 execuções, juntamente com um link para o dataset completo.
* **SHEETS:** https://docs.google.com/spreadsheets/d/19h3YJLsufQlPTEWUw43lo6ul1C4drDihqTBjJT74iOA/edit?usp=sharing 
  <img width="749" height="834" alt="HASH-EV" src="https://github.com/user-attachments/assets/39087387-0e79-47f7-8091-6ac45ff2c8d7" />


  5. **Análise dos Gráficos:**

 À atualizar: 
