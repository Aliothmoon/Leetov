package cn.hyy.leetov.proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.objenesis.ObjenesisHelper;

public class LimitProxyExecutor {
    private final Object original;
    private final Class<?> userClass;
    private static final Logger log = LoggerFactory.getLogger(LimitProxyExecutor.class);

    public LimitProxyExecutor(Object original, Class<?> userClass) {
        this.original = original;
        this.userClass = userClass;
    }

    public Object make() {
        return ObjenesisHelper.newInstance(generate());
    }

    public Class<?> generate() {
        return new ByteBuddy().subclass(userClass)
                .method(ElementMatchers.any())
                .intercept(InvocationHandlerAdapter.of(new LimitInvocationHandler(original)))
                .attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
                .annotateType(userClass.getAnnotations())
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();
    }

}
