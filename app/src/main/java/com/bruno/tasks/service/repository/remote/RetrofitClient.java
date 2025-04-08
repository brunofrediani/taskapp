    package com.bruno.tasks.service.repository.remote;

    import retrofit2.Retrofit;
    import retrofit2.converter.gson.GsonConverterFactory;


    public class RetrofitClient {

        private static String BASE_URL="https://www.devmasterteam.com/CursoAndroidAPI/";
        private static Retrofit retrofit;

        private RetrofitClient(){}

    private static Retrofit getRetrofitInstance(){
            if (retrofit==null){
                retrofit = new retrofit2.Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
    }
        public static <S> S createService(Class<S> serviceClass){
            return getRetrofitInstance().create(serviceClass);
        }
    }
