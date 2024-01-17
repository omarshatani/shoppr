Il problema che stai riscontrando è legato al comportamento asincrono di `CompletableFuture`. Quando chiami `dataSource.login(username, password)`, questo avvia un'operazione asincrona. Poiché l'esecuzione continua immediatamente, la probabilità che `result.isDone()` sia `true` subito dopo la chiamata è molto bassa, a meno che l'operazione non sia incredibilmente veloce o già completata per qualche altro motivo.

Inoltre, l'uso di `result.get()` immediatamente dopo senza attendere che il futuro sia completato potrebbe causare un'eccezione se l'operazione non è ancora terminata. `result.get()` è un metodo bloccante e attende il completamento del `CompletableFuture`.

Ecco come puoi gestire meglio il flusso asincrono:

### Opzione 1: Gestione Asincrona Completa
Modifica il metodo per restituire `CompletableFuture<Result<AuthResult>>` e gestisci il risultato asincronamente:

```java
public CompletableFuture<Result<AuthResult>> login(String username, String password) {
    return dataSource.login(username, password)
        .thenApply(authResult -> {
            Log.d("LOGIN SUCCESS", "YES");
            FirebaseUser user = authResult.getUser();
            setLoggedInUser(user);
            return new Result.Success<>(authResult);
        })
        .exceptionally(exception -> {
            Log.d("LOGIN FAIL", "Error: " + exception.getMessage());
            return new Result.Error<>(exception);
        });
}
```

In questo modo, il chiamante del metodo `login` dovrà gestire un `CompletableFuture` e decidere come e quando attendere il risultato.

### Opzione 2: Attendere Sincronicamente il Risultato
Se devi mantenere una logica sincrona, puoi aspettare il risultato in modo esplicito:

```java
public Result<AuthResult> login(String username, String password) throws ExecutionException, InterruptedException {
    CompletableFuture<AuthResult> result = dataSource.login(username, password);
    try {
        AuthResult authResult = result.get(); // Blocca e attende il completamento
        Log.d("LOGIN SUCCESS", "YES");
        FirebaseUser user = authResult.getUser();
        setLoggedInUser(user);
        return new Result.Success<>(authResult);
    } catch (Exception exception) {
        Log.d("LOGIN FAIL", "Error: " + exception.getMessage());
        return new Result.Error<>(exception);
    }
}
```

Questo approccio blocca il thread corrente fino a quando `dataSource.login` non ha completato l'esecuzione, il che può essere problematico se viene eseguito sul thread principale, specialmente in ambienti come Android.

### Scegliere l'Approccio Corretto
La scelta tra un approccio completamente asincrono e uno sincrono dipende dal contesto in cui la funzione viene utilizzata. Per applicazioni come quelle Android, è generalmente preferibile evitare operazioni bloccanti sul thread principale. In altri contesti, un approccio sincrono potrebbe essere accettabile se non causa problemi di prestazioni o di esperienza utente.