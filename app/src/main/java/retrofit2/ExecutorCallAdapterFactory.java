package retrofit2;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;
import okhttp3.Request;

final class ExecutorCallAdapterFactory extends CallAdapter$Factory {
    final Executor callbackExecutor;

    static final class ExecutorCallbackCall<T> implements Call<T> {
        final Executor callbackExecutor;
        final Call<T> delegate;

        ExecutorCallbackCall(Executor executor, Call<T> call) {
            this.callbackExecutor = executor;
            this.delegate = call;
        }

        public void enqueue(final Callback<T> callback) {
            Utils.checkNotNull(callback, "callback == null");
            this.delegate.enqueue(new Callback<T>() {
                public void onResponse(Call<T> call, final Response<T> response) {
                    ExecutorCallbackCall.this.callbackExecutor.execute(new Runnable() {
                        public void run() {
                            if (ExecutorCallbackCall.this.delegate.isCanceled()) {
                                callback.onFailure(ExecutorCallbackCall.this, new IOException("Canceled"));
                            } else {
                                callback.onResponse(ExecutorCallbackCall.this, response);
                            }
                        }
                    });
                }

                public void onFailure(Call<T> call, final Throwable th) {
                    ExecutorCallbackCall.this.callbackExecutor.execute(new Runnable() {
                        public void run() {
                            callback.onFailure(ExecutorCallbackCall.this, th);
                        }
                    });
                }
            });
        }

        public boolean isExecuted() {
            return this.delegate.isExecuted();
        }

        public Response<T> execute() throws IOException {
            return this.delegate.execute();
        }

        public void cancel() {
            this.delegate.cancel();
        }

        public boolean isCanceled() {
            return this.delegate.isCanceled();
        }

        public Call<T> clone() {
            return new ExecutorCallbackCall(this.callbackExecutor, this.delegate.clone());
        }

        public Request request() {
            return this.delegate.request();
        }
    }

    ExecutorCallAdapterFactory(Executor executor) {
        this.callbackExecutor = executor;
    }

    public CallAdapter<?, ?> get(Type type, Annotation[] annotationArr, Retrofit retrofit) {
        if (getRawType(type) != Call.class) {
            return null;
        }
        type = Utils.getCallResponseType(type);
        return new CallAdapter<Object, Call<?>>() {
            public Type responseType() {
                return type;
            }

            public Call<Object> adapt(Call<Object> call) {
                return new ExecutorCallbackCall(ExecutorCallAdapterFactory.this.callbackExecutor, call);
            }
        };
    }
}
