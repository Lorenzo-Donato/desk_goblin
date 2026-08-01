# 📜 GDD e System Prompt: Desk Goblin

**Contexto Principal:** Este documento serve como o System Prompt/Guia Arquitetural para agentes de IA (Opus, Sonnet, Gemini) auxiliando no desenvolvimento do jogo **Desk Goblin**. O projeto é um *point-and-click adventure* feito em **Java + libGDX**, mas com uma restrição técnica rigorosa: **não utilizar estruturas de dados nativas do Java (como `java.util.*`)**. Todas as estruturas principais devem ser implementadas do zero para atender aos requisitos da disciplina de Estrutura de Dados e Algoritmos.

---

## 🎮 1. Visão Geral do Jogo
* **Título:** Desk Goblin
* **Gênero:** Point-and-click adventure, puzzle de dedução, simulação (estilo *Papers, Please*).
* **Temática:** Fantasia medieval alta, absurdismo, humor peculiar.
* **Premissa:** O jogador controla a mão de um Goblin recepcionista em um hospital/abrigo de caridade medieval para pobres, peregrinos e doentes. A cura é feita com magia.
* **UI e Elementos da Mesa:**
  * 2 Orbes / Bolas de Cristal (ferramentas mágicas de análise/triagem).
  * 1 Pergaminho e 1 Tinteiro (para registro oficial de pacientes).
  * 2 Botões de Ação (ex: "Internar/Curar" e "Rejeitar", que finalizam o atendimento e chamam o próximo).
* **Game Loop Principal:** 
  1. Pacientes surgem na frente da mesa.
  2. O Goblin dialoga, cruza informações, analisa sintomas e identifica blefes usando os elementos da mesa (orbes, pergaminhos).
  3. O jogador usa os Botões para tomar a decisão final: designar o paciente ou expulsá-lo.

---

## 🏗️ 2. Restrições e Requisitos Técnicos
**Obrigatório:** O game loop deve ser intrinsecamente ligado às seguintes estruturas de dados desenvolvidas do zero (com testes JUnit 5 e logs de operação):

1. **Fila, Pilha e Lista Encadeada:**
   * A **Fila** DEVE ser implementada usando **duas Pilhas**.
   * As **Pilhas** DEVEM ser implementadas usando uma **Lista Simplesmente Encadeada** (iterativa ou recursiva).
2. **Árvore Binária de Busca (BST - AVL ou Preto-Vermelha):**
   * Deve ser balanceada.
3. **Heap Binária:**
   * Max Heap ou Min Heap. Usada para fila de prioridades ou ordenação (Heapsort).
4. **Tabela Hash:**
   * Deve tratar colisões com *Chaining* (usando a Lista Simplesmente Encadeada desenvolvida no item 1).

---

## 🧠 3. Design de Integração: Estruturas de Dados vs. Gameplay
*Atenção Agente: Quando for implementar funcionalidades do jogo, utilize OBRIGATORIAMENTE o mapeamento abaixo para justificar o uso das estruturas de dados.*

### A. A Fila de Espera (Fila com 2 Pilhas e Lista Encadeada)
* **Conceito no Jogo:** A fila literal de peregrinos e doentes aguardando atendimento do lado de fora do hospital.
* **Mecânica:** Sempre que o jogador toma uma decisão e clica em um dos **2 Botões** da mesa, o paciente atual sai de cena e o método `dequeue()` é chamado para trazer o próximo. O `enqueue()` ocorre em background conforme o tempo passa.

### B. O Grimório de Doenças e Regras (Árvore AVL ou PV)
* **Conceito no Jogo:** O manual de regras e sintomas.
* **Mecânica:** O jogo possui centenas de doenças mágicas. Para cruzar os sintomas ditos pelo paciente com a doença real, o sistema faz uma busca (`search()`) no Grimório em O(log n).

### C. O Sistema de Triagem / Urgência Médica (Max Heap)
* **Conceito no Jogo:** Pacientes em estado crítico que furam a fila ou precisam de realocação rápida nas macas.
* **Mecânica:** O jogador usa os **Orbes** para analisar a gravidade mágica do paciente. O orbe interage com a Max Heap para reorganizar os pacientes por grau de severidade (Prioridade O(1) para o mais grave).

### D. Registro de Pacientes Atendidos (Tabela Hash com Chaining)
* **Conceito no Jogo:** O arquivo final do dia, onde o **Tinteiro** e o **Pergaminho** registram o histórico de decisões.
* **Mecânica:** Cada paciente tem um documento/ID mágico único. Ao clicar nos botões de decisão, o resultado é inserido na Tabela Hash. O uso de encadeamento lida com mecânicas de "documentos falsificados" ou IDs mágicos duplicados (blefes).

---

## 🤖 4. Diretrizes de Ação para os Agentes de IA
Ao receber um prompt para codificar uma parte deste jogo, o agente DEVE:
1. **Zero Imports Proibidos:** NUNCA importe `java.util.Queue`, `java.util.Stack`, `java.util.LinkedList`, `java.util.HashMap`, etc.
2. **Separação MVC (LibGDX Core):** Mantenha as Estruturas de Dados (Model) isoladas da lógica de renderização gráfica do libGDX (View/Screen).
3. **Logs Extensivos:** Inclua `System.out.println` ou `Gdx.app.log` em cada inserção, remoção, rotação (AVL) e colisão (Hash) para facilitar a depuração e o vídeo de apresentação.
4. **Testes Unitários:** Forneça os testes JUnit 5 para cada estrutura antes de integrá-las ao libGDX.
