Problema específico
Ya que el gran problema encontrado por parte de los usuarios de Zonalibros era la dificultad en la recuperación de la contraseña el primer gran cambio fue hacer este proceso más simple, de tal manera que con un par de clicks y el correo electrónico pueda recuperarse.

Apartado visual
Los colores de la aplicación se inspiran en tonos cálidos y neutros del papel de los libros. Esta decisión busca evocar la sensación visual asociada a la lectura tradicional, creando una atmósfera familiar para el usuario

Apartado técnico
Se utilizó la arquitectura MVC (Modelo-Vista-Controlador) para mantener un orden dentro del proyecto y separar claramente la lógica del negocio, la interfaz de usuario y el flujo de datos. Esta separación facilita la mantención del proyecto a lo largo del tiempo.

Model
	Usuario.kt: Contiene la estructura que compone a cada usuario con su respectiva información, como el nombre, los géneros preferidos, su correo, etc. Incluye los posibles errores que pueda encontrar al intentar abrir su cuenta. El objetivo de separarlos en diferentes data class es modularizar el código y facilitar la validación de los datos ingresados por el usuario

Navigation
	AppNavigation.kt: Contiene la configuración de las rutas de uso que puede seguir el usuario. Esto se aplica para gestionar la transición entre las diferentes pantallas de la aplicación y que esta sea fluida y escalable

UI
	Screens: Contiene todas las vistas de la aplicación. Cada archivo dentro de este package representa una interfaz del flujo de usuario,  las cuales se separan para que el proyecto sea fácilmente mantenible en el tiempo

	Utills: Implementa un controlador de pantalla que distribuye los elementos de la aplicación dependiendo del largo y ancho disponible en el dispositivo. Con esto se evita escribir código en cada una de las vistas

ViewModel:
	LoginViewModel.kt: Controla el estado de la vista del login, es decir, valida los datos ingresados, gestiona los eventos de la vista y le comunica los resultados obtenidos para cambiar o no dependiendo de las validaciones

	RecuperacionViewModel.kt: Controla el estado de la vista de la recuperación de contraseña y valida los campos necesarios para lograr dicha recuperación.

	RegistroViewModel.kt: Maneja la lógica del registro de un nuevo usuario, validando los campos necesarios para lograr un registro exitoso.
	Todos los archivos dentro de viewmodel se modularizar con la finalidad de mantener la estructura del proyecto clara, ordenada y fácil de mantener
