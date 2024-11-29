# RestaurantTycoon

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a Kotlin project template that includes Kotlin application launchers and [KTX](https://libktx.github.io/) utilities.

## Concept

"Idle Gourmet Empire" is an idle restaurant management game where players start with a small diner and aim to grow it into a global restaurant chain. Players will manage staff, serve customers, expand their menu, and open new branches to build their gourmet empire. The game combines strategy with idle mechanics, allowing for progress even when the player is offline.

## Core Gameplay Mechanics

- **Waiter Serving Tables**: Waiters automatically serve customers, generating income. Players can upgrade waiter speed and efficiency.
- **Customer Queue with Satisfaction/Unsatisfaction**: Customers queue up to be served. A satisfaction meter decreases over time; faster service increases satisfaction and profits, while delays cause dissatisfaction and loss of income.
- **Expandable Menu with More Expensive Dishes**: Players can research and add new dishes to the menu, making it more diverse and expensive. Each dish has a research cost and time, impacting customer satisfaction and revenue.
- **Expandable Areas**: Players can expand their restaurant by purchasing additional areas, allowing for more tables, a larger kitchen, and more staff.
- **Buy Tables**: Players can buy more tables to accommodate more customers, increasing income potential but also requiring more staff and space.
- **Buy Branches**: After reaching certain milestones, players can open new branches in different locations, each with unique challenges and customer preferences.
- **Managing Staff Like Chef and Waiter**: Players hire, train, and manage staff, including chefs and waiters. Each staff member has unique skills and levels that can be upgraded.
- **Chef Skills**: Chefs have special skills that can be developed, such as faster cooking times, specialty dishes that increase satisfaction, and cost reduction techniques.

## Features
- **Idle Progression**: The restaurant earns revenue even when the player is offline, based on staff efficiency, menu diversity, and customer satisfaction levels.
- **Staff Management System**: A detailed staff management system where players can hire, fire, and train staff members. Staff satisfaction impacts their efficiency.
- **Dynamic Economy**: An in-game economy that reacts to player decisions, such as menu pricing, staff wages, and expansion costs.
- **Customer Feedback System**: Customers provide feedback on dishes, service speed, and restaurant ambiance, guiding players on what to improve.
- **Seasonal Events**: Limited-time events where players can earn exclusive dishes, decorations, and boosts by completing themed challenges.
- **Social Integration**: Players can visit friends' restaurants, compete in leaderboards, and exchange gifts.
- **Rewards System**: Players earn rewards for achieving milestones, such as expanding their restaurant, reaching certain income levels, or maintaining high satisfaction rates.

## Visual and Audio Aesthetics

- **Visuals**: A charming and colorful art style that is inviting and retains detail even when zoomed out to view the entire restaurant.
- **Audio**: Background music with relaxing, jazzy tunes for the restaurant ambiance. Sound effects for kitchen activities, customer chatter, and cash register sounds to enhance immersion.

## Monetization
 
- **In-App Purchases (IAPs)**: Players can purchase in-game currency to speed up research, instantly train staff, or buy exclusive items.
- **Ads**: Optional ads for players to watch in exchange for in-game bonuses, such as speed boosts or currency.
- **Subscription Model**: A VIP membership offering daily bonuses, exclusive items, and ad removal.

## Technical Requirements

- **Platform**: Mobile (iOS and Android) with potential future expansions to web and desktop.
- **Cloud Save**: Players can save their progress to the cloud and play on multiple devices.

## Marketing and Launch Strategy

- **Pre-Launch Campaign**: Social media teasers, influencer partnerships, and early access sign-ups.
- **Launch**: App Store and Google Play featured spots, launch trailer, and press releases.
- **Post-Launch**: Regular updates with new features, seasonal events, and community challenges to retain players.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.
- `android`: Android mobile platform. Needs Android SDK.
- `headless`: Desktop platform without a graphical interface.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `android:lint`: performs Android project validation.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `headless:run`: starts the headless application. Note: if headless sources were not modified - and the application still creates `ApplicationListener` from `core` project - this task might fail due to no graphics support.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
