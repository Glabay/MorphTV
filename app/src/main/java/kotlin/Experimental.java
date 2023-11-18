package kotlin;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.Retention;
import kotlin.annotation.Target;

@SinceKotlin(version = "1.3")
@Target(allowedTargets = {AnnotationTarget.ANNOTATION_CLASS})
@Retention(AnnotationRetention.BINARY)
@java.lang.annotation.Target({ElementType.ANNOTATION_TYPE})
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001:\u0002\u0007\bB\u001a\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005R\u000f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0000R\t\u0010\u0002\u001a\u00020\u0003¢\u0006\u0000¨\u0006\t"}, d2 = {"Lkotlin/Experimental;", "", "level", "Lkotlin/Experimental$Level;", "changesMayBreak", "", "Lkotlin/Experimental$Impact;", "Impact", "Level", "kotlin-stdlib-experimental"}, k = 1, mv = {1, 1, 10}, xi = 2)
@java.lang.annotation.Retention(RetentionPolicy.CLASS)
/* compiled from: Experimental.kt */
public @interface Experimental {

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, d2 = {"Lkotlin/Experimental$Impact;", "", "(Ljava/lang/String;I)V", "COMPILATION", "LINKAGE", "RUNTIME", "kotlin-stdlib-experimental"}, k = 1, mv = {1, 1, 10}, xi = 2)
    /* compiled from: Experimental.kt */
    public enum Impact {
    }

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004¨\u0006\u0005"}, d2 = {"Lkotlin/Experimental$Level;", "", "(Ljava/lang/String;I)V", "WARNING", "ERROR", "kotlin-stdlib-experimental"}, k = 1, mv = {1, 1, 10}, xi = 2)
    /* compiled from: Experimental.kt */
    public enum Level {
    }

    Impact[] changesMayBreak() default {Impact.COMPILATION, Impact.LINKAGE, Impact.RUNTIME};

    Level level() default Level.ERROR;
}
