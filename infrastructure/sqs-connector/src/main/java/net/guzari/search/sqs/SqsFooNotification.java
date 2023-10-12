package net.guzari.search.sqs;

import lombok.extern.slf4j.Slf4j;
import net.guzari.search.domain.foo.FooNotification;
import net.guzari.search.domain.foo.Response;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SqsFooNotification implements FooNotification {
    @Override
    public void notify(Response response) {
        log.info("Notification: {}", response);
    }
}
