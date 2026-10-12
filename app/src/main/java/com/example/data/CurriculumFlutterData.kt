package com.example.data

import com.example.model.*

object CurriculumFlutterData {

    val modules: List<CourseModule> = listOf(
        CourseModule(
            id = "fl_mod_1",
            track = CourseTrack.FLUTTER,
            order = 1,
            title = "1. Flutter Fundamentals",
            description = "Architecture, rendering pipeline, widget tree, hot reload vs restart, flutter doctor",
            iconName = "flutter",
            lessons = listOf(
                Lesson(
                    id = "fl_1_1",
                    courseTrack = CourseTrack.FLUTTER,
                    moduleId = "fl_mod_1",
                    moduleTitle = "Flutter Fundamentals",
                    title = "Flutter Architecture & The Three Trees",
                    description = "How Flutter draws every pixel on screen using the Widget, Element, and RenderObject trees.",
                    difficulty = Difficulty.BEGINNER,
                    durationMinutes = 10,
                    objectives = listOf(
                        "Understand Flutter's multi-layer architecture (Framework, Engine, Embedder)",
                        "Discover the Three Trees: Widget Tree, Element Tree, and RenderObject Tree",
                        "Learn why Flutter avoids native platform OEM widgets and draws directly via Impeller/Skia"
                    ),
                    analogy = "The Widget is a lightweight blueprint on paper. The Element is the construction manager on site. The RenderObject is the actual brick wall painted on the ground.",
                    detailedContent = """
Flutter is Google's UI toolkit that compiles to native machine code. Unlike React Native which bridges to native OEM UI controls, Flutter draws every single pixel directly using its C++ engine (Impeller/Skia) onto a Skia/Vulkan canvas.

The Three Trees:
1. Widget Tree: Declarative, lightweight, immutable configuration descriptions. Rebuilt constantly without performance penalties.
2. Element Tree: The structural backbone that manages the lifecycle of widgets and maintains references between widgets and render objects.
3. RenderObject Tree: Heavyweight objects that perform sizing, layout constraints, and actual painting on screen.
                    """.trimIndent(),
                    codeExample = """
import 'package:flutter/material.dart';

void main() {
  runApp(
    MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text('Flutter Academy Pro')),
        body: Center(
          child: Text('Hello Flutter Engine!'),
        ),
      ),
    ),
  );
}
                    """.trimIndent(),
                    expectedOutput = "flutter: Launching lib/main.dart on Android...\nflutter: Impeller graphics backend initialized.\nflutter: Frame rendered successfully at 60 FPS.",
                    walkthroughSteps = listOf(
                        LessonStep(1, "App Launch", "runApp(MaterialApp(...))", "Inflates the given widget and attaches it to the screen root."),
                        LessonStep(2, "Material Shell", "Scaffold(...)", "Provides the basic Material Design visual layout structure (app bar, body, drawers)."),
                        LessonStep(3, "Centering Widget", "Center(child: Text(...))", "Positions its child in the middle of the available space.")
                    ),
                    mistakes = listOf(
                        CommonMistake(
                            title = "Recreating Heavy Objects in build()",
                            wrongCode = "Widget build(BuildContext context) { final controller = AnimationController(...); }",
                            rightCode = "Initialize controllers in initState() of a StatefulWidget, not inside build().",
                            explanation = "build() methods can be called 60+ times per second. Creating expensive objects inside build causes severe frame drops (jank)."
                        )
                    ),
                    diagramType = "THREE_TREES",
                    diagramTitle = "Flutter Three Trees Architecture",
                    diagramDescription = "┌───────────────┐     ┌───────────────┐     ┌──────────────────┐\n│  Widget Tree  │ ──> │ Element Tree  │ ──> │ RenderObject Tree│\n│ (Blueprint)   │     │ (Manager)     │     │ (Layout & Paint) │\n└───────────────┘     └───────────────┘     └──────────────────┘",
                    challengePrompt = "Wrap the Center child in a Column and add two Text widgets.",
                    challengeSolution = "Column(\n  mainAxisAlignment: MainAxisAlignment.center,\n  children: [\n    Text('Welcome!'),\n    Text('Flutter is awesome!'),\n  ],\n)",
                    reviewQuestions = listOf(
                        QuizQuestion(
                            id = "q_fl_1_1_1",
                            topic = "Architecture",
                            track = CourseTrack.FLUTTER,
                            difficulty = Difficulty.BEGINNER,
                            type = QuestionType.SINGLE_CHOICE,
                            question = "Which of the three trees is responsible for measuring layout constraints and painting pixels on the screen?",
                            options = listOf("Widget Tree", "Element Tree", "RenderObject Tree", "State Tree"),
                            correctIndex = 2,
                            explanation = "The RenderObject Tree calculates exact geometry, handles hit-testing, and paints pixels."
                        )
                    )
                )
            )
        ),
        CourseModule(
            id = "fl_mod_2",
            track = CourseTrack.FLUTTER,
            order = 2,
            title = "2. Widgets and UI Layouts",
            description = "StatelessWidget vs StatefulWidget, BuildContext, Row, Column, Stack, ListView & responsive layouts",
            iconName = "widgets",
            lessons = listOf(
                Lesson(
                    id = "fl_2_1",
                    courseTrack = CourseTrack.FLUTTER,
                    moduleId = "fl_mod_2",
                    moduleTitle = "Widgets & UI",
                    title = "StatelessWidget vs StatefulWidget & Lifecycle",
                    description = "Master when to choose Stateless vs Stateful, and understand initState, didUpdateWidget, and dispose.",
                    difficulty = Difficulty.BEGINNER,
                    durationMinutes = 14,
                    objectives = listOf(
                        "Differentiate between immutable StatelessWidget and mutable StatefulWidget",
                        "Master the complete StatefulWidget lifecycle (initState -> didChangeDependencies -> build -> dispose)",
                        "Properly release resources (controllers, timers, streams) in dispose()"
                    ),
                    analogy = "A StatelessWidget is like a printed poster: once printed, it never changes. A StatefulWidget is like a digital LED scoreboard: it has internal memory and can update when the score changes.",
                    detailedContent = """
In Flutter, "Everything is a Widget".

1. StatelessWidget:
Used when the UI depends solely on configuration parameters passed into its constructor. It has no internal mutable state.

2. StatefulWidget:
Used when the widget needs to track internal state that changes across user interactions, timers, or network calls.
It splits into two classes:
• The Widget class (immutable configuration)
• The State<T> class (persists across re-builds)

State Lifecycle:
• initState(): Called exactly once when inserted into the tree. Initialize controllers and subscriptions here.
• build(BuildContext): Called whenever setState() is triggered. Returns the widget subtree.
• dispose(): Called when the widget is permanently removed from the tree. You MUST dispose AnimationControllers, TextEditingControllers, and cancel StreamSubscriptions here!
                    """.trimIndent(),
                    codeExample = """
import 'package:flutter/material.dart';

class CounterWidget extends StatefulWidget {
  const CounterWidget({super.key});

  @override
  State<CounterWidget> createState() => _CounterWidgetState();
}

class _CounterWidgetState extends State<CounterWidget> {
  int _count = 0;

  void _increment() {
    setState(() {
      _count++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Text('Counter Value: ${'$'}_count'),
        ElevatedButton(
          onPressed: _increment,
          child: const Text('Add +1'),
        ),
      ],
    );
  }
}
                    """.trimIndent(),
                    expectedOutput = "flutter: Initialized _CounterWidgetState\nflutter: build() rendered -> Counter Value: 0\nflutter: Button pressed -> setState() called -> Rebuilt with count: 1",
                    walkthroughSteps = listOf(
                        LessonStep(1, "State Creation", "createState() => _CounterWidgetState()", "Associates the mutable state object with the widget element."),
                        LessonStep(2, "Mutable Field", "int _count = 0;", "Private state field that survives widget rebuilds."),
                        LessonStep(3, "setState Call", "setState(() { _count++; });", "Notifies the framework that internal state has changed, scheduling a rebuild.")
                    ),
                    mistakes = listOf(
                        CommonMistake(
                            title = "Forgetting to dispose Controllers",
                            wrongCode = "TextEditingController _c = TextEditingController(); // No dispose method",
                            rightCode = "@override\nvoid dispose() {\n  _c.dispose();\n  super.dispose();\n}",
                            explanation = "Failing to dispose controllers causes permanent memory leaks in Flutter applications."
                        )
                    ),
                    diagramType = "STATE_LIFECYCLE",
                    diagramTitle = "StatefulWidget Lifecycle",
                    diagramDescription = "createState() ──> initState() ──> didChangeDependencies() ──> build()\n                                                                ▲\n                                     setState() ────────────────┘\n                                                                │\n                                                             dispose()",
                    challengePrompt = "Add a decrement button to the counter that reduces the value but stops at 0.",
                    challengeSolution = "void _decrement() {\n  if (_count > 0) {\n    setState(() => _count--);\n  }\n}",
                    reviewQuestions = listOf(
                        QuizQuestion(
                            id = "q_fl_2_1_1",
                            topic = "Lifecycle",
                            track = CourseTrack.FLUTTER,
                            difficulty = Difficulty.BEGINNER,
                            type = QuestionType.SINGLE_CHOICE,
                            question = "In which lifecycle method should you dispose animation controllers and text controllers?",
                            options = listOf("deactivate()", "dispose()", "stop()", "onDestroy()"),
                            correctIndex = 1,
                            explanation = "dispose() is called when the State object is removed permanently; clean up all controllers and subscriptions here."
                        )
                    )
                ),
                Lesson(
                    id = "fl_2_2",
                    courseTrack = CourseTrack.FLUTTER,
                    moduleId = "fl_mod_2",
                    moduleTitle = "Widgets & UI",
                    title = "Flex Layouts: Row, Column, Expanded & Flexible",
                    description = "Master horizontal and vertical layouts without encountering RenderFlex overflows.",
                    difficulty = Difficulty.BEGINNER,
                    durationMinutes = 12,
                    objectives = listOf(
                        "Understand MainAxisAlignment and CrossAxisAlignment in flex containers",
                        "Distinguish between Expanded (tight flex) and Flexible (loose flex)",
                        "Avoid the infamous 'A RenderFlex overflowed by xxx pixels' yellow-and-black striped error"
                    ),
                    analogy = "Row and Column are like train compartments. If you stuff rigid luggage that exceeds the compartment length, the doors can't close (Overflow!). Wrapping luggage in Expanded tells it to adapt its size to fit the available space.",
                    detailedContent = """
Flex rules in Flutter:
• Constraints go down.
• Sizes go up.
• Parent sets the position.

Row: lays out children horizontally.
Column: lays out children vertically.

MainAxisAlignment: Alignment along the primary axis (horizontal for Row, vertical for Column).
CrossAxisAlignment: Alignment along the perpendicular axis.

Expanded vs Flexible:
• Expanded forces the child to fill the entire remaining flex space (equivalent to Flexible with FlexFit.tight).
• Flexible allows the child to be smaller than the available space (FlexFit.loose).
                    """.trimIndent(),
                    codeExample = """
import 'package:flutter/material.dart';

Widget buildUserCard() {
  return Card(
    child: Padding(
      padding: const EdgeInsets.all(16.0),
      child: Row(
        children: [
          const CircleAvatar(child: Icon(Icons.person)),
          const SizedBox(width: 12.0),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: const [
                Text('Sardor Umarov', style: TextStyle(fontWeight: FontWeight.bold)),
                Text('Senior Flutter Architect', maxLines: 1, overflow: TextOverflow.ellipsis),
              ],
            ),
          ),
          IconButton(onPressed: () {}, icon: const Icon(Icons.more_vert)),
        ],
      ),
    ),
  );
}
                    """.trimIndent(),
                    expectedOutput = "flutter: Rendered UserCard layout with flexible title and fixed avatar.",
                    walkthroughSteps = listOf(
                        LessonStep(1, "Fixed Leading", "CircleAvatar(...)", "Takes fixed intrinsic size."),
                        LessonStep(2, "Flex Child", "Expanded(child: Column(...))", "Takes up all remaining horizontal space, preventing overflow on small screens."),
                        LessonStep(3, "Trailing Action", "IconButton(...)", "Placed cleanly on the right end.")
                    ),
                    mistakes = listOf(
                        CommonMistake(
                            title = "Placing Unbounded Text in a Row",
                            wrongCode = "Row(children: [Text('Very very long text that overflows screen')]);",
                            rightCode = "Row(children: [Expanded(child: Text('Long text safely wrapped'))]);",
                            explanation = "Without Expanded, Text calculates infinite width inside a Row, causing yellow/black striped overflow errors."
                        )
                    ),
                    diagramType = "FLEX_LAYOUT",
                    diagramTitle = "Row Flex Distribution",
                    diagramDescription = "[Avatar (40px)] + [Gap (12px)] + [Expanded: takes ALL remaining width] + [Icon (48px)]",
                    challengePrompt = "Build a two-column summary card showing 'Completed: 12' and 'Score: 98%' spaced evenly.",
                    challengeSolution = "Row(\n  mainAxisAlignment: MainAxisAlignment.spaceBetween,\n  children: [\n    Text('Completed: 12'),\n    Text('Score: 98%'),\n  ],\n)",
                    reviewQuestions = listOf(
                        QuizQuestion(
                            id = "q_fl_2_2_1",
                            topic = "Layouts",
                            track = CourseTrack.FLUTTER,
                            difficulty = Difficulty.BEGINNER,
                            type = QuestionType.SINGLE_CHOICE,
                            question = "What causes the 'A RenderFlex overflowed by ... pixels' error in Flutter?",
                            options = listOf(
                                "Children in a Row or Column exceeded the maximum available space along the main axis",
                                "The Dart memory limit was reached",
                                "The internet connection timed out",
                                "A missing BuildContext parameter"
                            ),
                            correctIndex = 0,
                            explanation = "Flex overflow occurs when unconstrained children exceed the bounded physical dimensions of a Row or Column."
                        )
                    )
                )
            )
        ),
        CourseModule(
            id = "fl_mod_3",
            track = CourseTrack.FLUTTER,
            order = 3,
            title = "3. State Management: Provider, Riverpod & Bloc",
            description = "Compare setState, InheritedWidget, Provider, Riverpod, and BLoC / Cubit architecture",
            iconName = "account_tree",
            lessons = listOf(
                Lesson(
                    id = "fl_3_1",
                    courseTrack = CourseTrack.FLUTTER,
                    moduleId = "fl_mod_3",
                    moduleTitle = "State Management",
                    title = "Modern State Management: Provider vs Bloc",
                    description = "Architect scalable applications using the BLoC pattern and Provider/ChangeNotifier.",
                    difficulty = Difficulty.INTERMEDIATE,
                    durationMinutes = 18,
                    objectives = listOf(
                        "Understand why prop drilling and pure setState fail in production apps",
                        "Implement ChangeNotifier and Provider for mid-size state",
                        "Architect unidirectional data flow using BLoC (Events -> States) and Cubits"
                    ),
                    analogy = "BLoC is like a bank teller window: you submit a formal transaction deposit slip (Event), the teller processes the business logic in the secure backroom, and hands you an updated account balance receipt (State).",
                    detailedContent = """
State Management Paradigms:
1. Ephemeral State: Local UI state (e.g. current tab, checkbox state, text field focus). Keep with setState()!
2. App State: Shared across multiple screens (e.g. user authentication, shopping cart, theme mode).

Provider & ChangeNotifier:
Extends ChangeNotifier, calls notifyListeners(), and consumers rebuild reactively. Simple to write, but relies heavily on BuildContext.

BLoC (Business Logic Component):
Separates presentation from business logic completely:
UI dispatches Event ──> Bloc processes business logic ──> UI receives new State.
Predictable, testable, and ideal for enterprise teams.
                    """.trimIndent(),
                    codeExample = """
// 1. Counter State
sealed class CounterState {
  final int count;
  const CounterState(this.count);
}
class CounterInitial extends CounterState {
  const CounterInitial() : super(0);
}
class CounterUpdated extends CounterState {
  const CounterUpdated(super.count);
}

// 2. Counter Cubit
class CounterCubit {
  CounterState _state = const CounterInitial();
  CounterState get state => _state;

  void increment() {
    _state = CounterUpdated(_state.count + 1);
    print("Cubit emitted new state: count = ${'$'}{_state.count}");
  }
}

void main() {
  final cubit = CounterCubit();
  cubit.increment();
  cubit.increment();
}
                    """.trimIndent(),
                    expectedOutput = "Cubit emitted new state: count = 1\nCubit emitted new state: count = 2",
                    walkthroughSteps = listOf(
                        LessonStep(1, "Sealed State", "sealed class CounterState", "Dart 3 sealed class guarantees exhaustiveness in UI switch statements."),
                        LessonStep(2, "Cubit Handler", "void increment()", "Direct method that executes logic and emits a new immutable state."),
                        LessonStep(3, "Unidirectional Flow", "UI -> Action -> New State", "UI never modifies state directly; it only observes emitted states.")
                    ),
                    mistakes = listOf(
                        CommonMistake(
                            title = "Mutating State Objects Directly",
                            wrongCode = "state.count = state.count + 1; emit(state); // Same instance!",
                            rightCode = "emit(CounterState(count: state.count + 1));",
                            explanation = "Bloc and Riverpod compare previous and next states by instance equality. Emitting the same mutated instance causes the UI not to rebuild."
                        )
                    ),
                    diagramType = "BLOC_FLOW",
                    diagramTitle = "BLoC Unidirectional Data Flow",
                    diagramDescription = "┌────────┐   UI Event    ┌────────┐   New State   ┌────────┐\n│ Widget │ ────────────> │  BLoC  │ ────────────> │ Widget │\n│   UI   │               │ Logic  │               │ Rebuild│\n└────────┘               └────────┘               └────────┘",
                    challengePrompt = "Add a reset() method to the CounterCubit that emits CounterInitial.",
                    challengeSolution = "void reset() {\n  _state = const CounterInitial();\n  print('Cubit reset to 0');\n}",
                    reviewQuestions = listOf(
                        QuizQuestion(
                            id = "q_fl_3_1_1",
                            topic = "State Management",
                            track = CourseTrack.FLUTTER,
                            difficulty = Difficulty.INTERMEDIATE,
                            type = QuestionType.SINGLE_CHOICE,
                            question = "In the BLoC pattern, how does the UI communicate user actions to the business logic component?",
                            options = listOf("By directly modifying private class fields", "By adding Events to the BLoC", "By calling setState() in the parent", "By restarting the Flutter engine"),
                            correctIndex = 1,
                            explanation = "In BLoC, the UI dispatches Events (e.g. FetchUsersEvent, SubmitFormEvent) which the BLoC maps to new States."
                        )
                    )
                )
            )
        ),
        CourseModule(
            id = "fl_mod_4",
            track = CourseTrack.FLUTTER,
            order = 4,
            title = "4. Clean Architecture & Networking",
            description = "Presentation, Domain, and Data layers, Repository pattern, Dio / http, and REST APIs",
            iconName = "layers",
            lessons = listOf(
                Lesson(
                    id = "fl_4_1",
                    courseTrack = CourseTrack.FLUTTER,
                    moduleId = "fl_mod_4",
                    moduleTitle = "Architecture & Networking",
                    title = "Clean Architecture Layers & Repository Pattern",
                    description = "Decouple presentation, use cases, and remote data sources for enterprise resilience.",
                    difficulty = Difficulty.ADVANCED,
                    durationMinutes = 20,
                    objectives = listOf(
                        "Structure projects into Presentation, Domain, and Data layers",
                        "Define abstract Repository interfaces in the Domain layer",
                        "Implement Network Data Sources using Dio/http and JSON serialization"
                    ),
                    analogy = "Domain is like the restaurant menu and chef recipes (pure business rules). Data is the farm delivery truck (APIs, databases). Presentation is the friendly waiter and dining table setting (Flutter UI).",
                    detailedContent = """
Clean Architecture Rules:
1. Domain Layer (Innermost, completely independent of Flutter):
   • Entities (Business models)
   • UseCases (Single-responsibility actions: e.g. GetArticlesUseCase)
   • Repository Interfaces (abstract contract)

2. Data Layer:
   • Models (DTOs with fromJson/toJson)
   • DataSources (RemoteApiClient, LocalDatabaseDao)
   • Repository Implementation (Implements Domain contract)

3. Presentation Layer:
   • Widgets & Screens
   • State Management (Bloc / Cubit / Riverpod)
                    """.trimIndent(),
                    codeExample = """
// 1. Domain: Entity & Abstract Contract
class Article {
  final String id;
  final String title;
  Article(this.id, this.title);
}

abstract class ArticleRepository {
  Future<List<Article>> getArticles();
}

// 2. Data: Implementation
class ArticleRepositoryImpl implements ArticleRepository {
  @override
  Future<List<Article>> getArticles() async {
    // Simulates HTTP response from REST API
    return [
      Article("1", "Flutter 3.29 Released"),
      Article("2", "Mastering Dart 3 Patterns"),
    ];
  }
}

void main() async {
  final repo = ArticleRepositoryImpl();
  final articles = await repo.getArticles();
  print("Fetched ${'$'}{articles.length} articles via Clean Architecture:");
  for (var a in articles) {
    print(" - ${'$'}{a.title}");
  }
}
                    """.trimIndent(),
                    expectedOutput = "Fetched 2 articles via Clean Architecture:\n - Flutter 3.29 Released\n - Mastering Dart 3 Patterns",
                    walkthroughSteps = listOf(
                        LessonStep(1, "Domain Contract", "abstract class ArticleRepository", "Domain defines the contract without caring whether data comes from REST, GraphQL, or SQLite."),
                        LessonStep(2, "Data Implementation", "class ArticleRepositoryImpl implements ...", "Implements the network and caching calls."),
                        LessonStep(3, "Dependency Inversion", "SOLID D-Principle", "High-level modules do not depend on low-level modules; both depend on abstractions.")
                    ),
                    mistakes = listOf(
                        CommonMistake(
                            title = "Importing Flutter in the Domain Layer",
                            wrongCode = "import 'package:flutter/material.dart'; // Inside domain/entities/user.dart",
                            rightCode = "Domain layer should have ZERO Flutter framework dependencies; pure Dart only.",
                            explanation = "Keeping Domain independent of Flutter allows unit testing without mocks and enables porting to backend Dart or web easily."
                        )
                    ),
                    diagramType = "CLEAN_ARCHITECTURE",
                    diagramTitle = "Clean Architecture Dependency Rule",
                    diagramDescription = "[Presentation (UI/Bloc)] ──> [Domain (Entities/UseCases)] <── [Data (API/DB)]\n                      (Dependencies always point inward!)",
                    challengePrompt = "Define a UseCase class GetArticlesUseCase that takes ArticleRepository as a constructor dependency.",
                    challengeSolution = "class GetArticlesUseCase {\n  final ArticleRepository repo;\n  GetArticlesUseCase(this.repo);\n  Future<List<Article>> execute() => repo.getArticles();\n}",
                    reviewQuestions = listOf(
                        QuizQuestion(
                            id = "q_fl_4_1_1",
                            topic = "Clean Architecture",
                            track = CourseTrack.FLUTTER,
                            difficulty = Difficulty.ADVANCED,
                            type = QuestionType.SINGLE_CHOICE,
                            question = "Why should the Domain layer avoid importing 'package:flutter/material.dart'?",
                            options = listOf(
                                "To keep business logic pure, independent of UI frameworks, and easily testable",
                                "Because Flutter doesn't compile files without widgets",
                                "To reduce network bandwidth",
                                "Because Flutter requires XML layout files"
                            ),
                            correctIndex = 0,
                            explanation = "Domain contains pure core business logic and rules. Decoupling it from the UI toolkit ensures clean testability and long-term maintainability."
                        )
                    )
                )
            )
        )
    )
}
