## Project Overview

It's my first Java project, and this project is a news management system developed using `JDK 17`, `Spring Boot 3`, and `Maven 3`. It provides functionalities for managing news and visitor data. The system also includes a recommendation feature that suggests news based on a user's browsing history.

It's also included a front-end application developed using `vue 3` to demonstrate the system's functionalities.


![img.png](img.png)
## Key Components

### Models
- `News`: Represents a news item with properties such as id, url, title, tag, imageUrl, content, publishTime, and views.
- `Visitor`: Represents a visitor with properties such as id, ipAddress, deviceType, newsId, timeStamp, and UUID.

### Repositories
- `NewsRepository`: Provides database operations for the News model.
- `VisitorRepository`: Provides database operations for the Visitor model.

### Services
- `NewsService`: Provides services related to news management such as getting all news, getting a specific news item, creating, updating, and deleting news.
- `VisitorService`: Provides services related to visitor management such as getting all visitors, getting a specific visitor, creating, updating, and deleting visitors.

### Controllers
- `NewsController`: Handles HTTP requests related to news management.
- `VisitorController`: Handles HTTP requests related to visitor management.

### Recommendation
- `Recommendation`: Provides a recommendation feature that suggests news based on a user's browsing history.

## Future Development

- Search Functionality: Implement a search feature to allow users to search for news articles by keywords.
- Pagination: Implement pagination for displaying news articles in manageable chunks.
- News Rating: Allow users to rate news articles.
- Advanced Recommendation: Improve the recommendation feature by considering other factors such as user ratings and user preferences.