package com.example.data

import com.example.model.*

object QuizBank {

    val questions: List<QuizQuestion> = listOf(
        // DART QUESTIONS
        QuizQuestion(
            id = "qb_dart_01",
            topic = "Dart Fundamentals",
            track = CourseTrack.DART,
            difficulty = Difficulty.BEGINNER,
            type = QuestionType.PREDICT_OUTPUT,
            question = "What is the output of the following Dart code snippet?",
            codeSnippet = "void main() {\n  var a = 10;\n  var b = 20;\n  print('\$a + \$b = \${a + b}');\n}",
            options = listOf("10 + 20 = \${a + b}", "10 + 20 = 30", "30", "Compile error"),
            correctIndex = 1,
            explanation = "\$a interpolates variable 'a' directly, while \${a + b} evaluates the expression inside curly braces to 30."
        ),
        QuizQuestion(
            id = "qb_dart_02",
            topic = "Null Safety",
            track = CourseTrack.DART,
            difficulty = Difficulty.INTERMEDIATE,
            type = QuestionType.FIND_BUG,
            question = "Which line in this Dart snippet causes a compile error under Sound Null Safety?",
            codeSnippet = "1: String? name = null;\n2: int length = name.length;\n3: print(length);",
            options = listOf("Line 1", "Line 2: Unchecked invocation on nullable 'name'", "Line 3", "None, it prints null"),
            correctIndex = 1,
            explanation = "Under Sound Null Safety, calling '.length' on a nullable variable without null checking (?. or ??) is a compile error."
        ),
        QuizQuestion(
            id = "qb_dart_03",
            topic = "Control Flow",
            track = CourseTrack.DART,
            difficulty = Difficulty.INTERMEDIATE,
            type = QuestionType.SINGLE_CHOICE,
            question = "In Dart 3, which operator is used for guards in pattern matching and switch statements?",
            options = listOf("if", "when", "guard", "where"),
            correctIndex = 1,
            explanation = "Dart 3 uses the 'when' keyword to apply guard clauses to patterns (e.g. case [int x, int y] when x > y => ...)."
        ),
        QuizQuestion(
            id = "qb_dart_04",
            topic = "OOP & Constructors",
            track = CourseTrack.DART,
            difficulty = Difficulty.INTERMEDIATE,
            type = QuestionType.SINGLE_CHOICE,
            question = "What is a unique property of factory constructors in Dart?",
            options = listOf(
                "They do not necessarily create a new instance of their class and can return cached instances or subtypes",
                "They cannot take arguments",
                "They run asynchronously on a background isolate",
                "They can only be called from inside a Flutter widget"
            ),
            correctIndex = 0,
            explanation = "A factory constructor can return an instance from a cache, return a subtype, or throw an exception rather than always allocating a fresh object."
        ),
        QuizQuestion(
            id = "qb_dart_05",
            topic = "Asynchrony",
            track = CourseTrack.DART,
            difficulty = Difficulty.ADVANCED,
            type = QuestionType.PREDICT_OUTPUT,
            question = "What order of execution will be printed?",
            codeSnippet = "void main() {\n  print('A');\n  Future(() => print('B'));\n  scheduleMicrotask(() => print('C'));\n  print('D');\n}",
            options = listOf("A, B, C, D", "A, D, C, B", "A, D, B, C", "C, A, D, B"),
            correctIndex = 1,
            explanation = "Synchronous code runs first ('A', 'D'). Then the Microtask Queue runs ('C'). Finally, the Event Queue runs ('B')."
        ),

        // FLUTTER QUESTIONS
        QuizQuestion(
            id = "qb_fl_01",
            topic = "Widgets & Trees",
            track = CourseTrack.FLUTTER,
            difficulty = Difficulty.BEGINNER,
            type = QuestionType.SINGLE_CHOICE,
            question = "What is the primary role of the BuildContext parameter passed to build()?",
            options = listOf(
                "It represents the location of the widget in the Element tree and allows looking up inherited widgets",
                "It holds the application's global network configuration",
                "It contains the phone's battery level",
                "It is a pointer to the Android Activity"
            ),
            correctIndex = 0,
            explanation = "BuildContext is a handle to the location of a widget in the Element Tree, used for traversing ancestors and retrieving Themes or Providers."
        ),
        QuizQuestion(
            id = "qb_fl_02",
            topic = "Keys in Flutter",
            track = CourseTrack.FLUTTER,
            difficulty = Difficulty.INTERMEDIATE,
            type = QuestionType.SINGLE_CHOICE,
            question = "When is a Key (like ValueKey or GlobalKey) essential in a Flutter widget?",
            options = listOf(
                "When reordering, inserting, or deleting stateful widgets in a collection of the same type",
                "On every single Text widget for styling",
                "Only when publishing an app to Google Play",
                "Keys are deprecated in Flutter 3+"
            ),
            correctIndex = 0,
            explanation = "Keys preserve state across widget swaps and removals in collections when multiple widgets share the exact same runtimeType."
        ),
        QuizQuestion(
            id = "qb_fl_03",
            topic = "Layout Constraints",
            track = CourseTrack.FLUTTER,
            difficulty = Difficulty.BEGINNER,
            type = QuestionType.SINGLE_CHOICE,
            question = "What is the golden rule of Flutter layout calculation?",
            options = listOf(
                "Constraints go down, sizes go up, parent sets position",
                "Sizes go down, constraints go up, child sets position",
                "Every widget decides its own screen position independently",
                "RenderFlex calculates margins automatically"
            ),
            correctIndex = 0,
            explanation = "Constraints flow down from parent to child, children report their desired sizes up to the parent, and the parent decides position."
        ),
        QuizQuestion(
            id = "qb_fl_04",
            topic = "State Management",
            track = CourseTrack.FLUTTER,
            difficulty = Difficulty.INTERMEDIATE,
            type = QuestionType.ARCHITECTURE,
            question = "Why is business logic separated from StatefulWidget into BLoC or Riverpod in enterprise apps?",
            options = listOf(
                "To make business logic unit-testable without UI dependencies and enable state sharing across disparate screens",
                "Because StatefulWidgets are slower to render",
                "Because Flutter does not allow network requests inside StatefulWidget",
                "To reduce APK download size"
            ),
            correctIndex = 0,
            explanation = "Separating business logic decouples pure algorithmic code from the UI layer, facilitating fast unit testing and clear separation of concerns."
        ),
        QuizQuestion(
            id = "qb_fl_05",
            topic = "Rendering Pipeline",
            track = CourseTrack.FLUTTER,
            difficulty = Difficulty.ADVANCED,
            type = QuestionType.SINGLE_CHOICE,
            question = "What graphics engine replaced Skia as the default renderer on iOS and modern Android in Flutter?",
            options = listOf("Impeller", "Vulkanite", "MetalEngine", "Blink"),
            correctIndex = 0,
            explanation = "Impeller is Flutter's modern graphics engine designed from scratch to eliminate shader compilation jank."
        )
    )

    fun getRandomMockExam(count: Int = 10): List<QuizQuestion> {
        return questions.shuffled().take(count)
    }

    fun getQuestionsForTrack(track: CourseTrack): List<QuizQuestion> {
        return questions.filter { it.track == track }
    }
}
