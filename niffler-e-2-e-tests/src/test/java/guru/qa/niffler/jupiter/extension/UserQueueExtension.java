package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.jupiter.annotation.User.Selector.FRIEND;
import static guru.qa.niffler.jupiter.annotation.User.Selector.INVITE_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.Selector.INVITE_SENT;
import static guru.qa.niffler.jupiter.annotation.User.Selector.TEST_USER;
import static guru.qa.niffler.model.UserJson.simpleUser;

// Любой тест проходит через него
public class UserQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UserQueueExtension.class);

    private static final Map<User.Selector,Queue<UserJson>> USERS = new ConcurrentHashMap<>();

    static {
        USERS.put(TEST_USER, new ConcurrentLinkedQueue<>(
                List.of(
                        simpleUser("ale1", "1234"))
        ));
        USERS.put(INVITE_SENT, new ConcurrentLinkedQueue<>(
                List.of(
                        simpleUser("ale2", "1234"))
        ));
        USERS.put(INVITE_RECEIVED, new ConcurrentLinkedQueue<>(
                List.of(
                        simpleUser("ale4", "1234"))
        ));
        USERS.put(FRIEND, new ConcurrentLinkedQueue<>(
                List.of(
                        simpleUser("ale3", "1234"))
        ));
    }


    @Override
    public void beforeEach(ExtensionContext context) {
        List<Method> methods = List.of(context.getRequiredTestClass().getDeclaredMethods());

        List<Parameter> parameters = methods.stream()
                .flatMap(method -> Arrays.stream(method.getParameters()))
                .filter(parameter -> parameter.isAnnotationPresent(User.class))
                .toList();

        Map<User.Selector, UserJson> users = new HashMap<>();

        for (Parameter parameter : parameters) {
            User.Selector selector = parameter.getAnnotation(User.class).selector();
            if (users.containsKey(selector)) {
                continue;
            }
            UserJson userForTest = null;
            Queue<UserJson> queue = USERS.get(selector);
            while (userForTest == null) {
                userForTest = queue.poll();
            }
            users.put(selector, userForTest);
        }
        context.getStore(NAMESPACE).put(context.getUniqueId(), users);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Map<User.Selector, UserJson> users = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        for (Map.Entry<User.Selector, UserJson> user : users.entrySet()) {
            USERS.get(user.getKey()).add(user.getValue());
        }
    }


    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class) && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        User.Selector selector = parameterContext.getParameter().getAnnotation(User.class).selector();
        return (UserJson) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class).get(selector);
    }
}
