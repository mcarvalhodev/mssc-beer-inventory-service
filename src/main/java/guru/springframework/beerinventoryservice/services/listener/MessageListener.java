package guru.springframework.beerinventoryservice.services.listener;

public interface MessageListener<T> {

  void listen(T payload);
}
