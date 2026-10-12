package com.example.data

import com.example.model.Flashcard

object FlashcardData {

    val flashcards: List<Flashcard> = listOf(
        Flashcard(
            id = "fc_01",
            category = "Flutter Core",
            term = "Widget",
            definition = "An immutable description of part of a user interface. In Flutter, almost everything (layouts, text, styling, gestures) is represented as a widget in a tree hierarchy.",
            codeSnippet = "const Text('Hello Flutter!')",
            bestPracticeTip = "Keep widgets small and focused on a single visual responsibility. Extract subtrees into separate StatelessWidget classes."
        ),
        Flashcard(
            id = "fc_02",
            category = "Flutter Core",
            term = "BuildContext",
            definition = "A handle to the location of a widget in the widget and element tree. It allows widgets to look up ancestors, themes, and inherited dependencies.",
            codeSnippet = "Theme.of(context).primaryColor\nMediaQuery.of(context).size.width",
            bestPracticeTip = "Never use BuildContext asynchronously across an 'await' boundary without first checking 'if (!context.mounted) return;'."
        ),
        Flashcard(
            id = "fc_03",
            category = "Dart Language",
            term = "Sound Null Safety",
            definition = "A static type guarantee where variables cannot be null unless explicitly marked with '?'. Guarantees no null pointer dereference crashes at runtime.",
            codeSnippet = "String name = 'Aziz'; // Non-nullable\nString? bio = null;  // Nullable",
            bestPracticeTip = "Avoid using the '!' null assertion operator in production code; prefer '?.' and '??' for crash-free fallbacks."
        ),
        Flashcard(
            id = "fc_04",
            category = "State Management",
            term = "BLoC (Business Logic Component)",
            definition = "An architectural pattern that uses reactive Streams to separate business logic from the UI. The UI dispatches Events, BLoC processes logic, and emits immutable States.",
            codeSnippet = "bloc.add(FetchDataEvent());\n// Emits StateLoading -> StateSuccess",
            bestPracticeTip = "Never put Flutter UI widgets or BuildContext instances inside your BLoC classes."
        ),
        Flashcard(
            id = "fc_05",
            category = "Dart Asynchrony",
            term = "Future vs Stream",
            definition = "A Future represents a single computation that eventually returns one value or an error. A Stream delivers an asynchronous sequence of multiple events over time.",
            codeSnippet = "Future<User> fetchUser();\nStream<Location> trackLocation();",
            bestPracticeTip = "Always cancel StreamSubscriptions in the dispose() method of your State to prevent memory leaks."
        ),
        Flashcard(
            id = "fc_06",
            category = "Architecture",
            term = "Repository Pattern",
            definition = "Mediates between the Domain/Business layer and Data sources (API, SQLite, Cache). Provides a clean collection-like interface to access domain entities.",
            codeSnippet = "abstract class UserRepository {\n  Future<User> getUser(String id);\n}",
            bestPracticeTip = "Declare the abstract repository interface in the Domain layer and place the concrete implementation in the Data layer."
        ),
        Flashcard(
            id = "fc_07",
            category = "UI & Rendering",
            term = "Slivers",
            definition = "A portion of a scrollable area that can be configured to achieve custom scrolling effects (such as sticky headers, expanding app bars, and mixed lists/grids).",
            codeSnippet = "CustomScrollView(\n  slivers: [SliverAppBar(), SliverList()],\n)",
            bestPracticeTip = "Use CustomScrollView with Slivers instead of nesting multiple SingleChildScrollView widgets to prevent scroll conflicts and memory bloat."
        ),
        Flashcard(
            id = "fc_08",
            category = "Dart Performance",
            term = "Isolate",
            definition = "A separate thread of execution with its own isolated memory heap and event loop. Used to perform CPU-intensive background tasks without dropping frames.",
            codeSnippet = "final result = await Isolate.run(() => heavyParsing(json));",
            bestPracticeTip = "Use Isolate.run() in modern Dart for quick one-off CPU tasks like large JSON decoding or image processing."
        )
    )
}
