package com.example.data

import com.example.model.*

object ProjectData {

    val projects: List<PracticalProject> = listOf(
        PracticalProject(
            id = "proj_01",
            title = "Modern To-Do & Habit Tracker",
            level = Difficulty.BEGINNER,
            category = "Productivity & Local Storage",
            summary = "Build a complete task manager featuring categorized tabs, local task persistence, animations, and progress metrics.",
            durationMinutes = 45,
            features = listOf(
                "Add, complete, and delete tasks",
                "Category tags (Work, Study, Personal)",
                "Local persistence via SharedPreferences or Hive",
                "Animated completion checkmarks and strike-throughs",
                "Empty state with custom illustration"
            ),
            folderTree = """
lib/
├── main.dart
├── models/
│   └── task_model.dart
├── repositories/
│   └── task_repository.dart
├── screens/
│   ├── home_screen.dart
│   └── add_task_dialog.dart
└── widgets/
    ├── task_tile.dart
    └── category_badge.dart
            """.trimIndent(),
            pubspecYaml = """
name: flutter_todo_pro
description: Production-grade Flutter Todo app.
dependencies:
  flutter:
    sdk: flutter
  shared_preferences: ^2.3.2
  uuid: ^4.5.1
            """.trimIndent(),
            architectureType = "MVVM with Repository Pattern",
            architectureExplanation = "Keeps UI screens separated from data storage. A TaskRepository handles persisting tasks to SharedPreferences, while ChangeNotifier provides reactive UI state.",
            steps = listOf(
                ProjectStep(
                    stepNumber = 1,
                    title = "Model Definition",
                    description = "Define the TaskModel with immutability, id, title, and fromMap/toMap serialization.",
                    codeSnippet = """
class Task {
  final String id;
  final String title;
  final bool isDone;
  final String category;

  const Task({
    required this.id,
    required this.title,
    this.isDone = false,
    this.category = 'General',
  });

  Task copyWith({bool? isDone}) => Task(
    id: id,
    title: title,
    isDone: isDone ?? this.isDone,
    category: category,
  );
}
                    """.trimIndent(),
                    fileName = "lib/models/task_model.dart"
                ),
                ProjectStep(
                    stepNumber = 2,
                    title = "Interactive Task Tile Widget",
                    description = "Build a responsive ListTile with Checkbox, smooth dismissal, and visual strike-through.",
                    codeSnippet = """
import 'package:flutter/material.dart';

class TaskTile extends StatelessWidget {
  final String title;
  final bool isDone;
  final ValueChanged<bool?> onToggle;
  final VoidCallback onDelete;

  const TaskTile({
    super.key,
    required this.title,
    required this.isDone,
    required this.onToggle,
    required this.onDelete,
  });

  @override
  Widget build(BuildContext context) {
    return Dismissible(
      key: Key(title),
      onDismissed: (_) => onDelete(),
      background: Container(color: Colors.red, alignment: Alignment.centerRight, padding: const EdgeInsets.only(right: 16), child: const Icon(Icons.delete, color: Colors.white)),
      child: ListTile(
        leading: Checkbox(value: isDone, onChanged: onToggle),
        title: Text(title, style: TextStyle(decoration: isDone ? TextDecoration.lineThrough : null)),
      ),
    );
  }
}
                    """.trimIndent(),
                    fileName = "lib/widgets/task_tile.dart"
                )
            ),
            commonBugs = listOf(
                "Attempting to modify final List directly inside State without setState",
                "Forgetting unique ValueKey in Dismissible, which crashes during quick deletes"
            ),
            extensionChallenges = listOf(
                "Add due date and time notifications using flutter_local_notifications",
                "Support dark mode switching with custom theme persistence"
            )
        ),
        PracticalProject(
            id = "proj_02",
            title = "REST API News & Article Explorer",
            level = Difficulty.INTERMEDIATE,
            category = "Networking & Clean Architecture",
            summary = "Architect a production-ready news app consuming real REST APIs with Dio, JSON models, shimmer loaders, and bookmarking.",
            durationMinutes = 60,
            features = listOf(
                "Connect to news REST API endpoints",
                "Automatic JSON deserialization with code generation",
                "Pull-to-refresh and infinite scroll pagination",
                "Shimmer skeleton loading placeholders",
                "Offline article caching and favorites"
            ),
            folderTree = """
lib/
├── core/
│   ├── network/dio_client.dart
│   └── errors/failures.dart
├── features/news/
│   ├── data/
│   │   ├── models/article_model.dart
│   │   └── repositories/news_repo_impl.dart
│   ├── domain/
│   │   ├── entities/article.dart
│   │   └── usecases/get_news_usecase.dart
│   └── presentation/
│       ├── bloc/news_bloc.dart
│       └── pages/news_feed_page.dart
└── main.dart
            """.trimIndent(),
            pubspecYaml = """
name: flutter_news_explorer
dependencies:
  flutter:
    sdk: flutter
  dio: ^5.7.0
  flutter_bloc: ^8.1.6
  shimmer: ^3.0.0
            """.trimIndent(),
            architectureType = "Feature-First Clean Architecture with BLoC",
            architectureExplanation = "Strictly isolates domain logic (entities and use cases) from external networking packages (Dio). The presentation layer reacts cleanly to Loading, Success, and Failure states.",
            steps = listOf(
                ProjectStep(
                    stepNumber = 1,
                    title = "Dio Client & Interceptor Setup",
                    description = "Configure base URL, timeouts, and auth headers cleanly in a singleton DioClient.",
                    codeSnippet = """
import 'package:dio/dio.dart';

class DioClient {
  final Dio dio = Dio(
    BaseOptions(
      baseUrl: 'https://newsapi.org/v2/',
      connectTimeout: const Duration(seconds: 10),
      receiveTimeout: const Duration(seconds: 10),
      headers: {'Accept': 'application/json'},
    ),
  );
}
                    """.trimIndent(),
                    fileName = "lib/core/network/dio_client.dart"
                ),
                ProjectStep(
                    stepNumber = 2,
                    title = "News BLoC State & Event Management",
                    description = "Handle FetchNewsEvent and emit NewsLoading, NewsSuccess, and NewsError states.",
                    codeSnippet = """
sealed class NewsState {}
class NewsLoading extends NewsState {}
class NewsSuccess extends NewsState {
  final List<dynamic> articles;
  NewsSuccess(this.articles);
}
class NewsError extends NewsState {
  final String message;
  NewsError(this.message);
}
                    """.trimIndent(),
                    fileName = "lib/features/news/presentation/bloc/news_bloc.dart"
                )
            ),
            commonBugs = listOf(
                "Crashing on null image URLs from third-party APIs (always provide a fallback placeholder!)",
                "Memory leak when navigating away while HTTP request is in-flight without CancellationToken"
            ),
            extensionChallenges = listOf(
                "Implement offline-first reading using SQLite/Drift database caching",
                "Add speech-to-text to read article contents aloud"
            )
        ),
        PracticalProject(
            id = "proj_03",
            title = "Clean Architecture E-Commerce Catalog",
            level = Difficulty.ADVANCED,
            category = "Enterprise Architecture & State",
            summary = "Full-fledged shopping app with shopping cart BLoC, animated checkout, filter chips, and modular dependencies.",
            durationMinutes = 90,
            features = listOf(
                "Product grid with sticky category filter chips",
                "Reactive Shopping Cart badge and checkout calculation",
                "Hero transition animations from grid to product details",
                "Discount promo-code validation engine",
                "Comprehensive unit and widget tests"
            ),
            folderTree = """
lib/
├── core/theme/app_theme.dart
├── features/
│   ├── catalog/
│   │   ├── domain/entities/product.dart
│   │   └── presentation/widgets/product_card.dart
│   └── cart/
│       ├── domain/entities/cart_item.dart
│       └── presentation/bloc/cart_bloc.dart
└── main.dart
            """.trimIndent(),
            pubspecYaml = """
name: flutter_shop_clean
dependencies:
  flutter:
    sdk: flutter
  flutter_bloc: ^8.1.6
  equatable: ^2.0.7
  cached_network_image: ^3.4.1
            """.trimIndent(),
            architectureType = "Clean Architecture with BLoC & Dependency Injection (get_it)",
            architectureExplanation = "Cart state is decoupled into its own independent BLoC so multiple screens (catalog, detail, cart drawer, checkout) can access and modify the basket smoothly.",
            steps = listOf(
                ProjectStep(
                    stepNumber = 1,
                    title = "Product Card with Hero Animation",
                    description = "Build the product card with image caching and Hero tag for smooth route transitions.",
                    codeSnippet = """
import 'package:flutter/material.dart';

class ProductCard extends StatelessWidget {
  final String id;
  final String title;
  final double price;
  final VoidCallback onAdd;

  const ProductCard({
    super.key,
    required this.id,
    required this.title,
    required this.price,
    required this.onAdd,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 2,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Hero(
            tag: 'product_${'$'}{id}',
            child: Container(height: 120, color: Colors.blue.shade50, child: const Center(child: Icon(Icons.shopping_bag, size: 48))),
          ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text(title, style: const TextStyle(fontWeight: FontWeight.bold)),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text('  \$${'$'}{price.toStringAsFixed(2)}'),
              IconButton(onPressed: onAdd, icon: const Icon(Icons.add_shopping_cart)),
            ],
          )
        ],
      ),
    );
  }
}
                    """.trimIndent(),
                    fileName = "lib/features/catalog/presentation/widgets/product_card.dart"
                )
            ),
            commonBugs = listOf(
                "Duplicate Hero tags across items in the GridView causing route transition exceptions",
                "Rounding floating-point errors in financial tax/total calculations"
            ),
            extensionChallenges = listOf(
                "Integrate Stripe payment sheet via flutter_stripe",
                "Implement AR product preview with ARCore/ARKit"
            )
        )
    )

    fun getProjectById(id: String): PracticalProject? {
        return projects.firstOrNull { it.id == id }
    }
}
