# Desk Goblin

Desk Goblin é um jogo 2D de gerenciamento de hospital, feito em Java com [libGDX](https://libgdx.com/). No papel de um atendente (o "goblin da mesa"), o jogador recebe pacientes no balcão, cadastra suas fichas, organiza a fila por gravidade e os interna em macas para acompanhar seu tratamento — tudo em tempo real.

O projeto nasceu como estudo de estruturas de dados clássicas aplicadas a um caso de uso concreto: cada mecânica de jogo (fila de prioridade, prontuário, leitos, processos médicos) é sustentada por uma estrutura de dados implementada do zero, sem uso das coleções prontas do Java.

## Como jogar

1. **Tela inicial** → clique para ver o tutorial → clique novamente para começar a partida.
2. Pacientes aparecem periodicamente no balcão (indicados por um `!`). Clique no paciente para puxar a ficha de identificação e ouvir sua queixa.
3. Use o **Pergaminho** (mesa) para:
   - Cadastrar um paciente (nome, ID, gravidade de 1 a 6);
   - Buscar um paciente cadastrado por ID;
   - Visualizar a árvore AVL de prontuários em tempo real.
4. Use o **Orbe esquerdo** para ver a fila de prioridade (min-heap por gravidade) e digitar uma maca (`M1`–`M12`) para internar o próximo paciente da fila.
5. Use o **Orbe direito** para inspecionar uma maca específica: paciente internado, gravidade e o progresso do processo médico (6 etapas cronometradas).
6. `ESC` pausa o jogo (tela de opções, com opção de voltar à tela inicial).

## Estruturas de dados

Implementadas em `core/src/main/java/com/deskgoblin/model/datastructures/`, sem depender de `java.util.*`:

| Estrutura | Uso no jogo |
|---|---|
| `AVLTree` | Prontuário de pacientes, indexado por ID, com percurso em ordem para listagem |
| `MinHeap` | Fila de atendimento, priorizando o paciente mais grave |
| `HashTable` | Mapeamento de macas (`M1`–`M12`) por ID |
| `QueueWithTwoStacks` | Fila de processos médicos ativos (FIFO implementada com duas pilhas) |
| `SinglyLinkedList` | Snapshots e listagens (ex.: percurso da AVL, fila de espera) |
| `Stack` | Estrutura de apoio usada pela fila de duas pilhas |

## Arquitetura

```
core/src/main/java/com/deskgoblin/
├── DeskGoblinGame.java        # ponto de entrada do jogo (libGDX Game)
├── model/
│   ├── HospitalManager.java   # orquestra as estruturas de dados acima
│   ├── datastructures/        # implementações próprias (ver tabela)
│   └── entities/               # Patient, Bed, Disease, MedicalProcess, MedicalRegistry
└── screens/
    ├── StartScreen.java        # menu inicial
    ├── TutorialScreen.java     # instruções
    ├── GameScreen.java         # tela principal do jogo
    └── OptionsScreen.java      # pausa

desktop/src/main/java/com/deskgoblin/desktop/
└── DesktopLauncher.java        # inicializa o backend LWJGL3 (janela 1280x720)
```

O projeto é um multi-módulo Gradle:
- **`core`** — lógica de jogo e estruturas de dados, independente de plataforma (`java-library`).
- **`desktop`** — launcher desktop via LWJGL3 (`application`).

## Requisitos

- JDK 21
- Não é necessário instalar o Gradle — o projeto usa o Gradle Wrapper (`./gradlew`)

## Rodando o jogo

```bash
./gradlew desktop:run
```

## Rodando os testes

Os testes (JUnit 5) cobrem principalmente as estruturas de dados e entidades do `core`:

```bash
./gradlew core:test
```

## Gerando o executável

```bash
./gradlew desktop:dist
```

Gera um JAR único (fat jar) em `desktop/build/libs/DeskGoblin.jar`, incluindo os assets e todas as dependências.

## Tecnologias

- Java 21
- [libGDX](https://libgdx.com/) 1.13.1 (com backend LWJGL3 e FreeType para fontes)
- JUnit 5 para testes
- Gradle (Kotlin DSL) com build multi-módulo

## Assets

Fontes, sprites e texturas ficam em `assets/` (fonte pixelada `VT323`, ícones de macas, orbes, pergaminho, etc.), carregados como arquivos internos do libGDX em tempo de execução.
