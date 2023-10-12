package net.guzari.search.domain.foo;

import org.springframework.stereotype.Service;

@Service
public class DefaultFooService implements FooService {
    private final FooRepository fooRepository;
    private final FooNotification fooNotification;

    public DefaultFooService(FooRepository fooRepository, FooNotification fooNotification) {
        this.fooRepository = fooRepository;
        this.fooNotification = fooNotification;
    }

    @Override
    public Response foo(Request request) {
        Response response = fooRepository.findFooData(request.getData());

        fooNotification.notify(response);

        return response;
    }
}
