# Práctica TCP – Hundir la flota

## Funcionalidad del servidor

- El servidor esperará a que se conecten al menos dos clientes. A partir de ahí, esperará un tiempo de 30 segundos para aceptar peticiones de registro de más clientes.

- Cada vez que reciba una petición de registro, enviará un mensaje de confirmación:

```
#REG_OK#
```

- Pasado ese tiempo, ningún cliente se podrá conectar. El servidor calculará el tamaño del tablero en función de los clientes conectados:

| Clientes | Tablero  |
|----------|---------|
| 2        | 10x10   |
| 3        | 12x12   |
| 4        | 13x13   |
| 5        | 14x14   |
| ...      | ...     |

- El servidor enviará a cada cliente el siguiente mensaje:

```
#TAB,n#
```

- Posteriormente, el servidor generará las posiciones de los barcos de cada jugador con la siguiente distribución:

    - 1 barco de longitud 4
    - 2 barcos de longitud 3
    - 1 barco de longitud 2

Y enviará el mensaje con la posición de todos sus barcos:

```
#POS%(A,1)(A,2)(A,3)(A,4),(C,3)(D,3)(E,3),(F,7)(F,8)(F,9),(H,9)(H,10)#
```

- El servidor enviará el mensaje de inicio de partida a todos los clientes:

```
#INICIO#
```

- El servidor enviará al primer cliente el token que le da el turno durante un tiempo. Si pasado ese tiempo no hay respuesta, correrá el turno.

- Cuando reciba un tiro de un cliente, deberá consultar si ha acertado y enviar la respuesta correspondiente:

    - **Sin acierto:**
    
    ```
    #AGUA#
    ```

    - **Acierto sin hundir el barco:**
    
    ```
    #TOCADO#
    ```

    - **Barco hundido:**
    
    ```
    #HUNDIDO#
    ```

    En caso de hundimiento, enviará el siguiente mensaje a todos los usuarios:

    ```
    #BARCO,TAMAÑO,USUARIO#
    ```

    Cuando un jugador pierda todos sus barcos, se enviará el mensaje:

    ```
    #FIN#USUARIO#
    ```

    El juego finalizará cuando solo quede un jugador, en cuyo caso el servidor enviará a todos los clientes:

    ```
    #GANADOR#USUARIO#
    ```

---

## Funcionalidad del cliente

1. El cliente deberá enviar una petición de registro al servidor mediante el mensaje:

   ```
   #REG#
   ```

2. Esperará a recibir la confirmación de registro del servidor.
3. Después esperará a recibir el mensaje con las posiciones de sus barcos.
4. A continuación, debe esperar al inicio de la partida.
5. Debe esperar a que el servidor le indique su turno de jugada:

   ```
   #turno#tiempo#
   ```

6. Cuando reciba el turno, el cliente enviará el mensaje al servidor con la posición de ataque:

   ```
   #TIRO(F,C)#
   ```

   Luego, deberá esperar la contestación del servidor sobre su acierto en el tiro.
7. Finalmente, el cliente se quedará a la espera del siguiente turno.

