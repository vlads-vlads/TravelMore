# TravelMore

TravelMore is the final project developed as part of the Accenture Java Bootcamp. It is a web application designed to facilitate travel planning, trip management, and social interaction among travelers. Whether you're planning your next adventure or sharing your travel experiences with others, TravelMore offers a comprehensive platform to streamline the entire travel process.

## Features

### User Authentication and Authorization

- Users can create accounts, log in, and log out securely.
- Authentication is implemented using JSON Web Tokens (JWT) to ensure secure communication between the client and server.
- Role-based access control ensures that only authorized users can access certain features and data.

### Trip Management

- Users can create, view, update, and delete trips.
- Trips can be personalized with descriptions, destinations, start and end dates, and participant lists.
- Advanced trip search functionality allows users to discover new destinations and find travel companions.

### Social Interaction

- Users can connect with other travelers, join trips, and interact with trip participants through comments.
- Social features foster a sense of community and enable users to share their travel experiences, tips, and recommendations.

### Image and Comment Management

- Users can upload images and leave comments on trips to share their experiences and provide feedback.
- Comments are displayed in real-time, allowing for instant communication and engagement among users.

## Technologies Used

- **Java**: Backend development is done primarily in Java, leveraging Spring Boot for building robust and scalable web applications.
- **Spring Security**: Implements user authentication, authorization, and role-based access control to ensure data security.
- **Spring Data JPA**: Provides a powerful and efficient way to interact with the database using Java Persistence API (JPA) entities.
- **JWT (JSON Web Tokens)**: Ensures secure authentication and communication between the client and server.
- **MySQL**: A relational database management system used for storing user data, trip information, comments, and images.
- **Thymeleaf**: Used for server-side HTML rendering to create dynamic and responsive web pages.
- **JavaScript**: Enhances user interaction and dynamic content loading on the client side.
- **Bootstrap**: Provides pre-designed UI components and responsive layouts for a visually appealing user interface.

## Getting Started

To run the TravelMore application locally, follow these steps:

1. Clone the repository: `git clone https://github.com/vlads-vlads/TravelMore.git`
2. Navigate to the project directory: `cd TravelMore`
3. Build the project using Maven: `mvn clean install`
4. Run the application: `java -jar target/TravelMore-0.0.1-SNAPSHOT.jar`
5. Access the application in your web browser at `https://localhost:8443`


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

TravelMore was developed as the final project for the Accenture Java Bootcamp. We would like to express our gratitude to our trainers, mentors, and peers who supported us throughout the bootcamp and helped us bring this project to life.