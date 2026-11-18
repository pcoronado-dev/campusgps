PROMPT1: Implementa la funcionalidad de **Seguimiento en vivo de ubicación** en mi aplicación Android utilizando Kotlin y Jetpack Compose.

Requisitos:
- Utiliza `requestLocationUpdates` para obtener actualizaciones continuas de ubicación mientras el usuario se mueve.
- Convierte las actualizaciones de ubicación en un `Flow` mediante `callbackFlow`.
- Implementa correctamente `awaitClose` para detener las actualizaciones y liberar recursos cuando el Flow se cierre.
- Organiza la funcionalidad siguiendo la arquitectura existente del proyecto: Repository + ViewModel + View.
- El Repository debe encargarse de obtener y emitir las ubicaciones en tiempo real.
- El ViewModel debe recoger el Flow utilizando `viewModelScope` y actualizar el estado de la UI.
- La View debe mostrar la ubicación actualizada en el mapa.
- Implementa el botón Iniciar/Detener seguimiento.
- Solicita los permisos de ubicación necesarios de forma correcta.
- Maneja los casos donde el usuario deniegue permisos o el GPS esté desactivado.
- Evita duplicar código y respeta la estructura actual del proyecto.
- No modifiques funcionalidades existentes que no estén relacionadas con el seguimiento.

Al finalizar, verifica que los cambios compilen correctamente y enumera los archivos modificados.

PROMPT2: Implementa la funcionalidad **Último lugar visitado** en mi aplicación Android utilizando Kotlin y Jetpack Compose.

Requisitos:
- Utiliza Preferences DataStore para guardar el último lugar seleccionado por el usuario.
- Crea o adapta un Repository que permita guardar y recuperar el último lugar visitado.
- Guarda la información necesaria del lugar seleccionado, como nombre, latitud y longitud.
- Implementa las operaciones de guardar y recuperar utilizando corrutinas y Flow cuando corresponda.
- Conecta el Repository con el ViewModel.
- El ViewModel debe recuperar automáticamente el último lugar guardado al iniciar la aplicación.
- Cuando el usuario seleccione un nuevo lugar, guarda automáticamente ese lugar en DataStore.
- Al abrir nuevamente la aplicación, recupera el último lugar y actualiza la interfaz y el mapa con su información.
- Maneja correctamente el caso en que todavía no exista ningún lugar guardado.
- Respeta la arquitectura y estructura de carpetas existentes.
- No elimines ni alteres funcionalidades actuales.

Al finalizar, verifica que la funcionalidad esté integrada y que el proyecto compile correctamente. Indica qué archivos creaste o modificaste.

PROMPT3: Implementa la funcionalidad **Mapa en modo oscuro** en mi aplicación Android utilizando Kotlin y Jetpack Compose.

Requisitos:
- El mapa debe cambiar automáticamente a un estilo oscuro cuando la aplicación esté utilizando el tema oscuro.
- Utiliza `MapStyleOptions` para configurar el estilo oscuro del mapa.
- Detecta el tema actual de la aplicación utilizando el sistema de temas de Compose, por ejemplo `isSystemInDarkTheme()` o la configuración de tema existente del proyecto.
- Cuando el tema sea oscuro, aplica el estilo oscuro al mapa.
- Cuando el tema sea claro, restaura el estilo normal del mapa.
- El cambio debe realizarse dinámicamente sin necesidad de reiniciar la aplicación.
- Mantén los marcadores, rutas, ubicación actual y demás elementos del mapa funcionando correctamente.
- Si el proyecto utiliza Google Maps Compose, integra la configuración con el componente de mapa existente.
- No modifiques otras pantallas ni el diseño general innecesariamente.
- Respeta la arquitectura y los archivos actuales del proyecto.

Al finalizar, verifica que el mapa cambie correctamente entre tema claro y oscuro y que el proyecto compile sin errores. Indica los archivos modificados.

1. ¿Por qué FusedLocationProviderClient está en el Repository y no en el ViewModel?

Porque el ViewModel solo debe manejar los datos de la pantalla, no hablar con Google. El Repository es como un “encargado” que sabe cómo conseguir la ubicación. Si algún día cambio de Google a otra forma de obtenerla, solo cambio al encargado y el resto de la app no se toca.

2. ¿Qué diferencia hay entre el permiso de ubicación y la clave de API de Maps?

El permiso es el “sí” del usuario para que la app vea dónde está. Si falta, no sé dónde está el usuario, pero el mapa sí se ve.
La clave de API es mi “credencial” ante Google para poder mostrar el mapa. Si falta, el mapa sale gris, pero la ubicación sí funciona.

3. ¿Por qué la clave va en local.properties y no en el código? ¿Qué haría si se subiera a GitHub?

Porque el código se sube a GitHub y cualquiera lo puede ver. Si la clave está ahí, otra persona podría usarla a mi nombre. En local.properties se queda solo en mi computadora.

Si se subiera por error, la borraría en Google Cloud, crearía una nueva y la pondría solo en local.properties.

4. ¿Dónde calculo la distancia y por qué no en el Composable?

La calculo en el ViewModel. El Composable solo debe dibujar lo que le entregan; si calcula, hace trabajo de más cada vez que se redibuja y mezcla cosas que deben estar separadas.

5. ¿Qué pasa si el usuario niega el permiso dos veces?

La app no se cierra. Android deja de preguntar y la app muestra un mensaje de error con el botón Abrir ajustes. Ese botón lleva al usuario a la configuración de la app para que active el permiso a mano.




<img width="702" height="1600" alt="WhatsApp Image 2026-11-18 at 4 37 05 AM" src="https://github.com/user-attachments/assets/e15ef302-bd04-4c57-8eec-5e16ee1bb94d" />
<img width="702" height="1600" alt="WhatsApp Image 2026-11-18 at 4 37 05 AM (1)" src="https://github.com/user-attachments/assets/aaeee0ef-5c77-4823-828d-45c3292cca5e" />
<img width="702" height="1600" alt="WhatsApp Image 2026-11-18 at 4 37 05 AM (2)" src="https://github.com/user-attachments/assets/f4d4f865-5961-4fe4-8733-bfa76764d5a2" />

