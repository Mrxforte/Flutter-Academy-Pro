package com.example.data

import com.example.model.*

object CurriculumDartData {

    val modules: List<CourseModule> = listOf(
        CourseModule(
            id = "dart_mod_1",
            track = CourseTrack.DART,
            order = 1,
            title = "1. Programming Fundamentals",
            description = "Dart basics, main() entry point, variables (var, final, const), type system & operators",
            iconName = "code",
            lessons = listOf(
                Lesson(
                    id = "dart_1_1",
                    courseTrack = CourseTrack.DART,
                    moduleId = "dart_mod_1",
                    moduleTitle = "Programming Fundamentals",
                    title = "Introduction to Dart & main()",
                    description = "Understand what Dart is, its dual compilation (JIT & AOT), and your first program.",
                    difficulty = Difficulty.BEGINNER,
                    durationMinutes = 8,
                    objectives = listOf(
                        "Understand Dart's role in powering Flutter applications",
                        "Learn why Dart supports both JIT (Just-In-Time) and AOT (Ahead-Of-Time) compilation",
                        "Write and execute your first Dart program with the main() function"
                    ),
                    analogy = "Think of main() as the front door of a building. When the Dart Virtual Machine enters your app, it always looks for this exact doorway first.",
                    detailedContent = """
Dart is a client-optimized programming language developed by Google. It is engineered for fast execution on mobile, desktop, and web platforms.

Key features of Dart:
• Strongly typed with sound null safety: prevents null dereference crashes at compile time.
• Dual compilation model: JIT enables instant Hot Reload during development, while AOT compiles directly to ARM/x64 machine code for 60/120fps production performance.
• Event-driven single-threaded execution with an event loop and isolates for heavy parallel tasks.

Every Dart app begins execution at top-level void main().
                    """.trimIndent(),
                    codeExample = """
void main() {
  print("Hello, Flutter Academy Pro!");
  var year = 2026;
  print("Mastering Dart & Flutter in " + year.toString());
}
                    """.trimIndent(),
                    expectedOutput = "Hello, Flutter Academy Pro!\nMastering Dart & Flutter in 2026",
                    walkthroughSteps = listOf(
                        LessonStep(1, "Entry Function", "void main()", "Declares the top-level entry point. The void return type indicates it returns no value."),
                        LessonStep(2, "Standard Output", "print(...);", "Prints string content to the console stdout. Every statement terminates with a semicolon ;."),
                        LessonStep(3, "Type Inference", "var year = 2026;", "Declares variable 'year'. Dart infers its type as int automatically.")
                    ),
                    mistakes = listOf(
                        CommonMistake(
                            title = "Missing Semicolon",
                            wrongCode = "print(\"Hello World\")",
                            rightCode = "print(\"Hello World\");",
                            explanation = "Dart requires explicit semicolons at the end of statements unlike Kotlin or Swift."
                        ),
                        CommonMistake(
                            title = "Capital M on Main",
                            wrongCode = "void Main() { }",
                            rightCode = "void main() { }",
                            explanation = "Dart is case-sensitive. The runtime entry point is strictly lowercase 'main'."
                        )
                    ),
                    diagramType = "COMPILATION_FLOW",
                    diagramTitle = "Dart Dual Compilation Architecture",
                    diagramDescription = "[Development] Dart Code ──> JIT (Just-In-Time) ──> Hot Reload (Sub-second UI update)\n[Production]  Dart Code ──> AOT (Ahead-Of-Time) ──> Native Machine Code (ARM64 / x86)",
                    challengePrompt = "Declare a variable named 'studentName' and print a greeting with string interpolation.",
                    challengeSolution = "void main() {\n  var studentName = 'Alex';\n  print('Welcome to Flutter, \$studentName!');\n}",
                    reviewQuestions = listOf(
                        QuizQuestion(
                            id = "q_dart_1_1_1",
                            topic = "Dart Fundamentals",
                            track = CourseTrack.DART,
                            difficulty = Difficulty.BEGINNER,
                            type = QuestionType.SINGLE_CHOICE,
                            question = "Which function serves as the mandatory execution entry point of every Dart program?",
                            options = listOf("start()", "run()", "main()", "init()"),
                            correctIndex = 2,
                            explanation = "The main() function is the mandatory top-level entry point in Dart."
                        ),
                        QuizQuestion(
                            id = "q_dart_1_1_2",
                            topic = "Compilation",
                            track = CourseTrack.DART,
                            difficulty = Difficulty.BEGINNER,
                            type = QuestionType.SINGLE_CHOICE,
                            question = "What enables Flutter's sub-second Hot Reload during active development?",
                            options = listOf("Ahead-Of-Time (AOT) compilation", "Just-In-Time (JIT) compilation", "Java Bytecode Interpreter", "V8 Web Engine"),
                            correctIndex = 1,
                            explanation = "JIT compilation allows the Dart VM to inject code modifications into a running app without a full rebuild."
                        )
                    )
                ),
                Lesson(
                    id = "dart_1_2",
                    courseTrack = CourseTrack.DART,
                    moduleId = "dart_mod_1",
                    moduleTitle = "Programming Fundamentals",
                    title = "Variables, Types, final & const",
                    description = "Master var, Object, dynamic, final vs const, and String interpolation.",
                    difficulty = Difficulty.BEGINNER,
                    durationMinutes = 10,
                    objectives = listOf(
                        "Distinguish between var, dynamic, and Object",
                        "Master the critical difference between final (runtime constant) and const (compile-time constant)",
                        "Use string interpolation (\$variable and \${expression}) effectively"
                    ),
                    analogy = "final is like a Polaroid photo: once snapped (at runtime), you cannot change it. const is like a statue sculpted at the factory (at compile time); it exists immutably before the program even runs.",
                    detailedContent = """
Dart is a statically typed language with rich type inference.

Variable Modifiers:
1. var: Type is inferred once at declaration. Cannot change type later.
2. final: Can only be set once at runtime. Memory is allocated when evaluated.
3. const: Immutable compile-time constant. Canonicalized in memory (identical const objects share memory).
4. dynamic: Disables static type analysis; method calls are checked at runtime. Use rarely!

String Interpolation:
Use ${'$'}variableName or ${'$'}{expression.toUpperCase()} inside quotes.
                    """.trimIndent(),
                    codeExample = """
void main() {
  const double taxRate = 0.08;
  final DateTime now = DateTime.now();
  var score = 95;
  String message = "Score: ${'$'}score, Tax: ${'$'}{taxRate * 100}%";
  print(message);
}
                    """.trimIndent(),
                    expectedOutput = "Score: 95, Tax: 8.0%",
                    walkthroughSteps = listOf(
                        LessonStep(1, "Compile-time Constant", "const double taxRate = 0.08;", "Fixed value baked into the binary at compile time."),
                        LessonStep(2, "Runtime Constant", "final DateTime now = DateTime.now();", "Evaluated once when program runs. Cannot be const because current time is unknown at compile time."),
                        LessonStep(3, "String Interpolation", "\${taxRate * 100}%", "Evaluates arbitrary expressions inside \${...}.")
                    ),
                    mistakes = listOf(
                        CommonMistake(
                            title = "Using const for Runtime Values",
                            wrongCode = "const time = DateTime.now();",
                            rightCode = "final time = DateTime.now();",
                            explanation = "DateTime.now() cannot be computed at compile time; therefore it must be 'final', not 'const'."
                        )
                    ),
                    diagramType = "CONSTANT_MATRIX",
                    diagramTitle = "final vs const Comparison",
                    diagramDescription = "┌──────────────┬───────────────────┬───────────────────┐\n│ Feature      │ final             │ const             │\n├──────────────┼───────────────────┼───────────────────┤\n│ Assignment   │ Once at runtime   │ Once at compile   │\n│ Canonicalized│ No                │ Yes (Shared mem)  │\n│ Deep Immut.  │ Top-level only    │ Deeply immutable  │\n└──────────────┴───────────────────┴───────────────────┘",
                    challengePrompt = "Create a const appVersion and a final launchedDate, then print them in a formatted sentence.",
                    challengeSolution = "void main() {\n  const appVersion = '3.5.0';\n  final launchedDate = DateTime(2026, 1, 1);\n  print('App v\$appVersion launched at \$launchedDate');\n}",
                    reviewQuestions = listOf(
                        QuizQuestion(
                            id = "q_dart_1_2_1",
                            topic = "Variables",
                            track = CourseTrack.DART,
                            difficulty = Difficulty.BEGINNER,
                            type = QuestionType.PREDICT_OUTPUT,
                            question = "What happens if you assign a String to a variable declared with 'var number = 10;'?",
                            codeSnippet = "var number = 10;\nnumber = 'ten';",
                            options = listOf("Prints 'ten' without error", "Compile error: A value of type 'String' cannot be assigned to 'int'", "Runtime NullPointerException", "Automatic type coercion"),
                            correctIndex = 1,
                            explanation = "Dart's 'var' statically infers 'int' on initialization. Changing types later produces a compile error."
                        )
                    )
                )
            )
        ),
        CourseModule(
            id = "dart_mod_2",
            track = CourseTrack.DART,
            order = 2,
            title = "2. Control Flow & Pattern Matching",
            description = "Conditionals, switch expressions (Dart 3+), loops, and guard clauses",
            iconName = "alt_route",
            lessons = listOf(
                Lesson(
                    id = "dart_2_1",
                    courseTrack = CourseTrack.DART,
                    moduleId = "dart_mod_2",
                    moduleTitle = "Control Flow",
                    title = "Switch Expressions & Pattern Matching",
                    description = "Learn Dart 3 modern pattern matching and concise switch expressions.",
                    difficulty = Difficulty.INTERMEDIATE,
                    durationMinutes = 12,
                    objectives = listOf(
                        "Learn Dart 3 pattern matching syntax",
                        "Replace verbose switch statements with modern switch expressions",
                        "Utilize guard clauses with the 'when' keyword"
                    ),
                    analogy = "A switch expression is like a smart postal sorting chute: each parcel slides into the matching slot and directly delivers its payload in one clean pass.",
                    detailedContent = """
In Dart 3, switch was transformed from a cumbersome statement into an expressive, exhaustiveness-checked expression.

Syntax:
val result = switch (target) {
  pattern1 => value1,
  pattern2 when guardCondition => value2,
  _ => defaultValue,
};

Exhaustiveness checking ensures you never miss a case when switching over enums or sealed classes!
                    """.trimIndent(),
                    codeExample = """
void main() {
  var status = 200;
  var message = switch (status) {
    200 => "OK: Success",
    404 => "Not Found",
    500 => "Internal Server Error",
    _ => "Unknown Status",
  };
  print("HTTP Result: ${'$'}message");
}
                    """.trimIndent(),
                    expectedOutput = "HTTP Result: OK: Success",
                    walkthroughSteps = listOf(
                        LessonStep(1, "Switch Expression", "switch (status)", "Evaluates 'status' as an expression that returns a direct value."),
                        LessonStep(2, "Arrow Mapping", "200 => \"OK: Success\",", "Binds pattern to return value without needing 'case' or 'break'."),
                        LessonStep(3, "Wildcard Catch-All", "_ => \"Unknown Status\",", "Underscore wildcard matches any unhandled values.")
                    ),
                    mistakes = listOf(
                        CommonMistake(
                            title = "Missing Wildcard in Non-exhaustive Switch",
                            wrongCode = "var msg = switch(status) { 200 => 'OK' }; // Error if status is 404",
                            rightCode = "var msg = switch(status) { 200 => 'OK', _ => 'Other' };",
                            explanation = "Switch expressions must be exhaustive, meaning every possible value must be handled."
                        )
                    ),
                    diagramType = "SWITCH_PATTERN",
                    diagramTitle = "Dart 3 Switch Flow",
                    diagramDescription = "Target Value ──> [Pattern 1 ?] ──(yes)──> Return Val 1\n               └──(no)──> [Pattern 2 ?] ──(yes)──> Return Val 2\n               └──(no)──> [_ Wildcard] ─────────> Return Default",
                    challengePrompt = "Write a switch expression that maps user roles ('admin', 'editor', 'guest') to their permission levels.",
                    challengeSolution = "void main() {\n  var role = 'admin';\n  var perms = switch(role) {\n    'admin' => 'Full Access',\n    'editor' => 'Edit Access',\n    _ => 'Read Only',\n  };\n  print('Permissions: \$perms');\n}",
                    reviewQuestions = listOf(
                        QuizQuestion(
                            id = "q_dart_2_1_1",
                            topic = "Switch Expressions",
                            track = CourseTrack.DART,
                            difficulty = Difficulty.INTERMEDIATE,
                            type = QuestionType.SINGLE_CHOICE,
                            question = "What symbol is used as the default fallback wildcard in Dart 3 switch expressions?",
                            options = listOf("default", "*", "_", "else"),
                            correctIndex = 2,
                            explanation = "In Dart 3 switch expressions, the underscore '_' serves as the wildcard pattern for any unmatched case."
                        )
                    )
                )
            )
        ),
        CourseModule(
            id = "dart_mod_3",
            track = CourseTrack.DART,
            order = 3,
            title = "3. Functions & Closures",
            description = "Named & optional parameters, arrow syntax, higher-order functions, and closures",
            iconName = "functions",
            lessons = listOf(
                Lesson(
                    id = "dart_3_1",
                    courseTrack = CourseTrack.DART,
                    moduleId = "dart_mod_3",
                    moduleTitle = "Functions",
                    title = "Named Parameters & Arrow Functions",
                    description = "Why Flutter uses named parameters everywhere and how to write clean functions.",
                    difficulty = Difficulty.BEGINNER,
                    durationMinutes = 10,
                    objectives = listOf(
                        "Understand positional vs named parameters in Dart",
                        "Learn the 'required' modifier and default parameter values",
                        "Use concise arrow functions (=>) for single expressions"
                    ),
                    analogy = "Positional arguments are like numbered seats at an auditorium; you must sit in order. Named arguments are like labeled seat reservations with your name tags; order does not matter!",
                    detailedContent = """
Flutter widgets rely heavily on Dart named parameters. For example:
Container(width: 100, height: 200, color: Colors.blue)

Parameter Types:
1. Positional: void greet(String name, int age)
2. Optional Positional: void greet(String name, [int? age])
3. Named: void greet({required String name, int age = 18})

Enclosing parameters in curly braces { } makes them named.
                    """.trimIndent(),
                    codeExample = """
void createUser({
  required String username,
  String role = "student",
  bool isActive = true,
}) {
  print("User: ${'$'}username | Role: ${'$'}role | Active: ${'$'}isActive");
}

void main() {
  createUser(username: "Aziz", role: "flutter_lead");
  createUser(username: "Elena");
}
                    """.trimIndent(),
                    expectedOutput = "User: Aziz | Role: flutter_lead | Active: true\nUser: Elena | Role: student | Active: true",
                    walkthroughSteps = listOf(
                        LessonStep(1, "Named Parameters", "{ required String username, ... }", "Parameters enclosed in {} are named. Callers pass them as key: value."),
                        LessonStep(2, "Default Values", "String role = \"student\"", "If omitted by the caller, the default value is used."),
                        LessonStep(3, "Required Modifier", "required String username", "Compile-time enforcement that callers must provide this value.")
                    ),
                    mistakes = listOf(
                        CommonMistake(
                            title = "Mixing Positional and Named without Brackets",
                            wrongCode = "void send(String to, String msg = 'Hi') { }",
                            rightCode = "void send(String to, {String msg = 'Hi'}) { }",
                            explanation = "Default values for optional arguments require brackets: [] for positional or {} for named."
                        )
                    ),
                    diagramType = "PARAM_SPECS",
                    diagramTitle = "Function Parameter Styles",
                    diagramDescription = "Positional:  fn(arg1, arg2)               <-- Order strictly matters\nOptional:    fn(arg1, [arg2 = 10])       <-- Order matters, optional\nNamed:       fn({required arg1, arg2})   <-- Order independent, labeled",
                    challengePrompt = "Write a function calculateDiscount({required double price, double percent = 10.0}) that prints the discounted total.",
                    challengeSolution = "void calculateDiscount({required double price, double percent = 10.0}) {\n  double total = price - (price * (percent / 100));\n  print('Final total: \$total');\n}\nvoid main() {\n  calculateDiscount(price: 100.0, percent: 20.0);\n}",
                    reviewQuestions = listOf(
                        QuizQuestion(
                            id = "q_dart_3_1_1",
                            topic = "Named Parameters",
                            track = CourseTrack.DART,
                            difficulty = Difficulty.BEGINNER,
                            type = QuestionType.SINGLE_CHOICE,
                            question = "Why does Flutter use named parameters for almost all widget constructors?",
                            options = listOf(
                                "To improve readability and avoid ambiguity with dozens of widget properties",
                                "Because positional parameters are deprecated in Dart",
                                "Because named parameters compile faster",
                                "To save RAM during runtime"
                            ),
                            correctIndex = 0,
                            explanation = "Named parameters allow widgets with many optional arguments to be configured cleanly without remembering exact positional order."
                        )
                    )
                )
            )
        ),
        CourseModule(
            id = "dart_mod_4",
            track = CourseTrack.DART,
            order = 4,
            title = "4. Collections & Transformations",
            description = "List, Set, Map, where/map/fold, spread operator, collection-if & for",
            iconName = "dataset",
            lessons = listOf(
                Lesson(
                    id = "dart_4_1",
                    courseTrack = CourseTrack.DART,
                    moduleId = "dart_mod_4",
                    moduleTitle = "Collections",
                    title = "Modern Collection Features & Functional Operators",
                    description = "Supercharge your lists with spread operators, collection-if, and map/where.",
                    difficulty = Difficulty.INTERMEDIATE,
                    durationMinutes = 14,
                    objectives = listOf(
                        "Use collection-if and collection-for inside UI widget lists",
                        "Unpack elements with spread (...) and null-aware spread (...?) operators",
                        "Chain .where(), .map(), and .toList() for declarative data transformations"
                    ),
                    analogy = "Collection-if is like packing your travel bag with conditional weather gear: if (isRaining) pack(umbrella). You build the list dynamically in place!",
                    detailedContent = """
In Flutter, lists are constantly constructed to build widget children trees: Column(children: [...]).

Dart provides syntax features tailored specifically for declarative UI building:
1. Collection-if: Include an item only when a condition is true: [itemA, if (isLoggedIn) itemB]
2. Collection-for: Generate items directly inside a literal: [for (var item in items) Widget(item)]
3. Spread operators (... and ...?): Unpack another collection into the current list.
                    """.trimIndent(),
                    codeExample = """
void main() {
  bool isAdmin = true;
  var baseNav = ["Home", "Search"];
  var fullNav = [
    ...baseNav,
    if (isAdmin) "Admin Panel",
    for (var i = 1; i <= 2; i++) "Tab ${'$'}i",
  ];
  print("Navigation Items: ${'$'}fullNav");

  var numbers = [1, 2, 3, 4, 5, 6];
  var evenSquares = numbers
      .where((n) => n % 2 == 0)
      .map((n) => n * n)
      .toList();
  print("Even Squares: ${'$'}evenSquares");
}
                    """.trimIndent(),
                    expectedOutput = "Navigation Items: [Home, Search, Admin Panel, Tab 1, Tab 2]\nEven Squares: [4, 16, 36]",
                    walkthroughSteps = listOf(
                        LessonStep(1, "Spread Operator", "...baseNav", "Unpacks all elements of baseNav into fullNav."),
                        LessonStep(2, "Collection-if", "if (isAdmin) \"Admin Panel\"", "Appends 'Admin Panel' only if isAdmin is true."),
                        LessonStep(3, "Functional Pipeline", ".where(...).map(...)", "Filters even numbers, squares each, and converts the lazy Iterable back to a List.")
                    ),
                    mistakes = listOf(
                        CommonMistake(
                            title = "Forgetting .toList()",
                            wrongCode = "List<int> result = numbers.map((n) => n * 2); // Error: Iterable is not List",
                            rightCode = "List<int> result = numbers.map((n) => n * 2).toList();",
                            explanation = ".map() produces a lazy Iterable<T>. Call .toList() when you need a concrete List."
                        )
                    ),
                    diagramType = "COLLECTION_PIPELINE",
                    diagramTitle = "Functional Collection Pipeline",
                    diagramDescription = "[1, 2, 3, 4, 5, 6] ──> [.where(even)] ──> [2, 4, 6] ──> [.map(n*n)] ──> [4, 16, 36]",
                    challengePrompt = "Filter a list of names to only those starting with 'A' and convert them to uppercase.",
                    challengeSolution = "void main() {\n  var names = ['Alice', 'Bob', 'Aziz', 'Charlie'];\n  var aNames = names.where((n) => n.startsWith('A')).map((n) => n.toUpperCase()).toList();\n  print(aNames);\n}",
                    reviewQuestions = listOf(
                        QuizQuestion(
                            id = "q_dart_4_1_1",
                            topic = "Collections",
                            track = CourseTrack.DART,
                            difficulty = Difficulty.INTERMEDIATE,
                            type = QuestionType.PREDICT_OUTPUT,
                            question = "What will be printed by the following collection-if snippet?",
                            codeSnippet = "bool loggedIn = false;\nvar nav = ['Home', if (loggedIn) 'Profile', 'Help'];\nprint(nav.length);",
                            options = listOf("3", "2", "1", "Compile error"),
                            correctIndex = 1,
                            explanation = "Because loggedIn is false, 'Profile' is not included. The list contains ['Home', 'Help'], so length is 2."
                        )
                    )
                )
            )
        ),
        CourseModule(
            id = "dart_mod_5",
            track = CourseTrack.DART,
            order = 5,
            title = "5. Object-Oriented Programming & SOLID",
            description = "Classes, named constructors, factory constructors, mixins, abstract classes & SOLID",
            iconName = "category",
            lessons = listOf(
                Lesson(
                    id = "dart_5_1",
                    courseTrack = CourseTrack.DART,
                    moduleId = "dart_mod_5",
                    moduleTitle = "OOP & SOLID",
                    title = "Constructors, Factory & Mixins",
                    description = "Master named constructors, json deserialization factory, and mixins with 'with'.",
                    difficulty = Difficulty.INTERMEDIATE,
                    durationMinutes = 16,
                    objectives = listOf(
                        "Write generative and named constructors with initializing formals (this.field)",
                        "Create factory constructors for singleton caching and JSON parsing",
                        "Use Mixins to share code across multiple class hierarchies without multiple inheritance"
                    ),
                    analogy = "A mixin is like adding a backpack or toolbelt to a person. It gives them specific capabilities (like Swimming or Logging) without changing their core identity (Human).",
                    detailedContent = """
Dart is fully object-oriented where every value is an instance of Object.

Key Constructor Patterns:
1. Initializing Formals: User(this.id, this.name);
2. Named Constructors: User.anonymous() : id = '0', name = 'Guest';
3. Factory Constructor: Returns an existing instance or subtype instead of always allocating fresh memory. Used for fromJson(Map) and Singletons!

Mixins:
Declared with 'mixin Feature' and added to a class using 'with Feature'. Enables high reusability without deep inheritance trees.
                    """.trimIndent(),
                    codeExample = """
mixin LoggerMixin {
  void log(String msg) => print("[LOG]: ${'$'}msg");
}

class User with LoggerMixin {
  final String id;
  final String name;

  // Generative constructor with initializing formals
  User({required this.id, required this.name});

  // Factory constructor for JSON deserialization
  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      id: json['id'] as String,
      name: json['name'] as String,
    );
  }
}

void main() {
  var u = User.fromJson({'id': 'u101', 'name': 'Sardor'});
  u.log("User initialized: ${'$'}{u.name}");
}
                    """.trimIndent(),
                    expectedOutput = "[LOG]: User initialized: Sardor",
                    walkthroughSteps = listOf(
                        LessonStep(1, "Mixin Declaration", "mixin LoggerMixin { ... }", "Defines reusable methods that can be composed into any class with 'with'."),
                        LessonStep(2, "Initializing Formals", "User({required this.id, required this.name});", "Assigns fields directly before the constructor body executes."),
                        LessonStep(3, "Factory Deserializer", "factory User.fromJson(Map json)", "Standard pattern for turning API JSON maps into typed model instances.")
                    ),
                    mistakes = listOf(
                        CommonMistake(
                            title = "Calling 'this' inside Factory before Construction",
                            wrongCode = "factory User.create() { this.id = '1'; } // Error",
                            rightCode = "factory User.create() { return User(id: '1', name: 'Default'); }",
                            explanation = "Factory constructors do not have access to 'this' because the instance is created only when returned."
                        )
                    ),
                    diagramType = "MIXIN_HIERARCHY",
                    diagramTitle = "Mixin Composition Model",
                    diagramDescription = "class Animal {}\nmixin Swimmer {}\nmixin Flyer {}\n\nclass Duck extends Animal with Swimmer, Flyer {}\n// Duck inherits from Animal and absorbs Swimmer + Flyer capabilities!",
                    challengePrompt = "Create a Product class with a factory constructor Product.fromJson(Map<String, dynamic> json).",
                    challengeSolution = "class Product {\n  final String title;\n  final double price;\n  Product(this.title, this.price);\n  factory Product.fromJson(Map<String, dynamic> j) => Product(j['title'], (j['price'] as num).toDouble());\n}\nvoid main() {\n  var p = Product.fromJson({'title': 'Laptop', 'price': 999.0});\n  print('\${p.title}: \${p.price}');\n}",
                    reviewQuestions = listOf(
                        QuizQuestion(
                            id = "q_dart_5_1_1",
                            topic = "Mixins",
                            track = CourseTrack.DART,
                            difficulty = Difficulty.INTERMEDIATE,
                            type = QuestionType.SINGLE_CHOICE,
                            question = "Which keyword is used to apply a mixin to a Dart class?",
                            options = listOf("extends", "implements", "with", "mixin"),
                            correctIndex = 2,
                            explanation = "The 'with' keyword is used to apply one or more mixins to a class declaration."
                        )
                    )
                )
            )
        ),
        CourseModule(
            id = "dart_mod_6",
            track = CourseTrack.DART,
            order = 6,
            title = "6. Advanced Dart: Asynchrony & Types",
            description = "Null safety, Futures, async/await, Streams, Records, and Sealed classes",
            iconName = "sync",
            lessons = listOf(
                Lesson(
                    id = "dart_6_1",
                    courseTrack = CourseTrack.DART,
                    moduleId = "dart_mod_6",
                    moduleTitle = "Advanced Dart",
                    title = "Sound Null Safety & Operators",
                    description = "Eliminate null pointer exceptions using ?, !, ??, and ??= operators.",
                    difficulty = Difficulty.ADVANCED,
                    durationMinutes = 14,
                    objectives = listOf(
                        "Understand Sound Null Safety in the Dart compiler",
                        "Use the null-aware operators (?, ??, ??=, !)",
                        "Work safely with the 'late' modifier"
                    ),
                    analogy = "Sound Null Safety is like airport security: types are non-nullable by default. Only types stamped with a '?' passport are allowed to carry null through the metal detector.",
                    detailedContent = """
Dart features Sound Null Safety. If a variable is declared as String, the compiler guarantees it can NEVER be null at runtime.

Null-Aware Operators:
1. Type? : Nullable type declaration (String? name)
2. ?. : Null-aware method invocation (name?.toUpperCase())
3. ?? : Fallback default operator (name ?? "Anonymous")
4. ??= : Null-coalescing assignment (cache ??= compute())
5. ! : Null assertion operator (use with caution: throws exception if null!)
6. late : Tells compiler the variable will be initialized before first read.
                    """.trimIndent(),
                    codeExample = """
void main() {
  String? nullableName;
  // Fallback with ??
  String displayName = nullableName ?? "Guest User";
  print("Welcome: ${'$'}displayName");

  nullableName = "Azamat";
  // Safe navigation with ?.
  print("Length: ${'$'}{nullableName?.length}");
}
                    """.trimIndent(),
                    expectedOutput = "Welcome: Guest User\nLength: 6",
                    walkthroughSteps = listOf(
                        LessonStep(1, "Nullable Variable", "String? nullableName;", "The question mark denotes that this variable may hold either a String or null."),
                        LessonStep(2, "Default Coalesce", "nullableName ?? \"Guest User\"", "If nullableName is null, the expression evaluates to \"Guest User\"."),
                        LessonStep(3, "Safe Navigation", "nullableName?.length", "Executes .length only if nullableName is not null; returns null otherwise.")
                    ),
                    mistakes = listOf(
                        CommonMistake(
                            title = "Overusing the ! Assertion",
                            wrongCode = "print(nullableUser!.name); // Crashes if user is null!",
                            rightCode = "print(nullableUser?.name ?? 'No user');",
                            explanation = "Avoid '!' whenever possible. Use '?.' and '??' for graceful, crash-free fallbacks."
                        )
                    ),
                    diagramType = "NULL_SAFETY",
                    diagramTitle = "Null Safety Type Hierarchy",
                    diagramDescription = "         Object (Non-nullable root)\n        /      \\\n      int     String\n\n      Null (Null singleton)\n        \\      /\n      int?    String? (Nullable union types)",
                    challengePrompt = "Safely read an address object from a nullable user profile, printing 'Address unknown' if either is null.",
                    challengeSolution = "void main() {\n  Map<String, dynamic>? profile;\n  String address = profile?['address']?['city'] ?? 'Address unknown';\n  print(address);\n}",
                    reviewQuestions = listOf(
                        QuizQuestion(
                            id = "q_dart_6_1_1",
                            topic = "Null Safety",
                            track = CourseTrack.DART,
                            difficulty = Difficulty.ADVANCED,
                            type = QuestionType.PREDICT_OUTPUT,
                            question = "What is the output of the following code?",
                            codeSnippet = "int? a;\nint b = a ?? 42;\nprint(b);",
                            options = listOf("null", "0", "42", "Throws TypeError"),
                            correctIndex = 2,
                            explanation = "Because 'a' is null, the '??' fallback operator evaluates to the right-hand operand, 42."
                        )
                    )
                ),
                Lesson(
                    id = "dart_6_2",
                    courseTrack = CourseTrack.DART,
                    moduleId = "dart_mod_6",
                    moduleTitle = "Advanced Dart",
                    title = "Futures, async/await & Streams",
                    description = "Master asynchronous Dart programming, the Event Loop, and real-time Streams.",
                    difficulty = Difficulty.ADVANCED,
                    durationMinutes = 18,
                    objectives = listOf(
                        "Understand Dart's single-threaded Event Loop and Microtask Queue",
                        "Handle asynchronous results with Future, async, and await",
                        "Listen and transform real-time event sequences with Stream and async*"
                    ),
                    analogy = "A Future is a buzzer from a restaurant: you order food, get the buzzer immediately, go sit down, and when the food is ready (completed), the buzzer rings. A Stream is a conveyor belt of sushi: dishes arrive continuously over time.",
                    detailedContent = """
Dart code runs in an isolate with a single event loop.

1. Future<T>: Represents a single computation that completes with a value of type T or an error in the future.
2. async & await: Provides synchronous-looking syntax for working with asynchronous code.
3. Stream<T>: Represents a sequence of asynchronous events over time (e.g., Firestore updates, websocket messages, location updates).
4. StreamController: Allows you to manually emit values into a stream via sink.add(value).
                    """.trimIndent(),
                    codeExample = """
Future<String> fetchUser() async {
  // Simulates network delay
  return Future.delayed(Duration(milliseconds: 50), () => "User #42");
}

Stream<int> countStream() async* {
  for (int i = 1; i <= 3; i++) {
    yield i;
  }
}

void main() async {
  print("Requesting user...");
  var user = await fetchUser();
  print("Received: ${'$'}user");

  print("Reading stream:");
  await for (var num in countStream()) {
    print("Stream emitted: ${'$'}num");
  }
}
                    """.trimIndent(),
                    expectedOutput = "Requesting user...\nReceived: User #42\nReading stream:\nStream emitted: 1\nStream emitted: 2\nStream emitted: 3",
                    walkthroughSteps = listOf(
                        LessonStep(1, "Async Function", "Future<String> fetchUser() async", "Marking a function as 'async' automatically packages its return value into a Future."),
                        LessonStep(2, "Await Execution", "var user = await fetchUser();", "Pauses execution of the current function until the Future completes without blocking the UI thread."),
                        LessonStep(3, "Generator Stream", "yield i;", "Emits values sequentially into the Stream using the 'yield' keyword.")
                    ),
                    mistakes = listOf(
                        CommonMistake(
                            title = "Forgetting to await a Future",
                            wrongCode = "String user = fetchUser(); // Error: Future<String> is not String",
                            rightCode = "String user = await fetchUser();",
                            explanation = "Without 'await', you receive the uncompleted Future object rather than the resolved value."
                        )
                    ),
                    diagramType = "EVENT_LOOP",
                    diagramTitle = "Dart Event Loop",
                    diagramDescription = "Microtask Queue (Highest Priority: scheduleMicrotask) ──┐\n                                                        ├──> Event Loop ──> Execution\nEvent Queue (I/O, Timers, Touch, Futures) ─────────────┘",
                    challengePrompt = "Write an async function that simulates fetching battery percentage and prints a warning if below 20%.",
                    challengeSolution = "Future<int> getBattery() async => 15;\nvoid main() async {\n  int level = await getBattery();\n  if (level < 20) print('Low Battery Warning: \$level%');\n}",
                    reviewQuestions = listOf(
                        QuizQuestion(
                            id = "q_dart_6_2_1",
                            topic = "Asynchrony",
                            track = CourseTrack.DART,
                            difficulty = Difficulty.ADVANCED,
                            type = QuestionType.SINGLE_CHOICE,
                            question = "What is the key difference between a Future and a Stream?",
                            options = listOf(
                                "A Future yields a single value or error; a Stream delivers multiple asynchronous events over time",
                                "Futures run in parallel threads while Streams run on the UI thread",
                                "Streams are synchronous; Futures are asynchronous",
                                "Futures can only return integers"
                            ),
                            correctIndex = 0,
                            explanation = "A Future represents a single eventual value, whereas a Stream is a continuous sequence of asynchronous values."
                        )
                    )
                )
            )
        ),
        CourseModule(
            id = "dart_mod_7",
            track = CourseTrack.DART,
            order = 7,
            title = "7. Testing & Professional Dart",
            description = "Unit tests, mocking, Effective Dart style rules, and pubspec.yaml configuration",
            iconName = "fact_check",
            lessons = listOf(
                Lesson(
                    id = "dart_7_1",
                    courseTrack = CourseTrack.DART,
                    moduleId = "dart_mod_7",
                    moduleTitle = "Testing & Professional Dart",
                    title = "Unit Testing & Effective Dart Style",
                    description = "Write test suites with test() and expect(), and follow Google's Effective Dart conventions.",
                    difficulty = Difficulty.ADVANCED,
                    durationMinutes = 14,
                    objectives = listOf(
                        "Structure unit tests using the test and flutter_test packages",
                        "Use expect() matchers (equals, isTrue, throwsA)",
                        "Follow Google Effective Dart naming and formatting guidelines"
                    ),
                    analogy = "Writing unit tests is like an engineer testing bridge cables in a laboratory before opening the bridge to heavy traffic.",
                    detailedContent = """
Effective Dart Guidelines:
• DO use lowerCamelCase for variables, functions, and parameter names.
• DO use UpperCamelCase for types, classes, enums, and extensions.
• DO use lowercase_with_underscores for file and library names.
• PREFER declaring types on public APIs.

Unit Testing with test():
group('MathTests', () {
  test('addition works', () {
    expect(2 + 2, equals(4));
  });
});
                    """.trimIndent(),
                    codeExample = """
int add(int a, int b) => a + b;

void runTests() {
  var result = add(10, 20);
  assert(result == 30, "Result must equal 30");
  print("✓ Test add(10, 20) == 30 PASSED");

  var negativeResult = add(-5, 5);
  assert(negativeResult == 0);
  print("✓ Test add(-5, 5) == 0 PASSED");
}

void main() {
  runTests();
  print("All unit tests verified successfully!");
}
                    """.trimIndent(),
                    expectedOutput = "✓ Test add(10, 20) == 30 PASSED\n✓ Test add(-5, 5) == 0 PASSED\nAll unit tests verified successfully!",
                    walkthroughSteps = listOf(
                        LessonStep(1, "Pure Function", "int add(int a, int b) => a + b;", "Pure functions without side effects are the easiest to test."),
                        LessonStep(2, "Assertion Check", "assert(result == 30);", "Verifies expected behavior. In test packages, expect(actual, equals(matcher)) is used."),
                        LessonStep(3, "Test Output", "print(\"PASSED\");", "Reports test status.")
                    ),
                    mistakes = listOf(
                        CommonMistake(
                            title = "Non-standard File Naming",
                            wrongCode = "myUserProfile.dart or MyUserProfile.dart",
                            rightCode = "my_user_profile.dart",
                            explanation = "Effective Dart requires all file and directory names to strictly be lowercase_with_underscores."
                        )
                    ),
                    diagramType = "TESTING_PYRAMID",
                    diagramTitle = "Flutter Testing Pyramid",
                    diagramDescription = "       /\\   Integration Tests (Few, Slow, Full App E2E)\n      /  \\  Widget Tests (Medium, Fast Component Tests)\n     /____\\ Unit Tests (Many, Ultra-fast Business Logic)",
                    challengePrompt = "Write a function isEven(int n) and test it with both even and odd numbers.",
                    challengeSolution = "bool isEven(int n) => n % 2 == 0;\nvoid main() {\n  assert(isEven(4) == true);\n  assert(isEven(5) == false);\n  print('isEven tests passed!');\n}",
                    reviewQuestions = listOf(
                        QuizQuestion(
                            id = "q_dart_7_1_1",
                            topic = "Testing",
                            track = CourseTrack.DART,
                            difficulty = Difficulty.ADVANCED,
                            type = QuestionType.SINGLE_CHOICE,
                            question = "According to Effective Dart, how should Dart file names be formatted?",
                            options = listOf("camelCase.dart", "PascalCase.dart", "lowercase_with_underscores.dart", "kebab-case.dart"),
                            correctIndex = 2,
                            explanation = "Effective Dart strictly prescribes lowercase_with_underscores for file and directory names."
                        )
                    )
                )
            )
        )
    )
}
