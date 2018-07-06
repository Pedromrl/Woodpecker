package com.example.pedrolemos.livrosfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity {

    private int sleeptimer = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Criar um objeto e chamar a classe
        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();
    }

    //Pôr o splashscreen a funcionar
    private class LogoLauncher extends Thread{
        public void run(){
            try{
                //Se funcionar espera 1 segundo
                sleep(1000 * sleeptimer);
            }catch(InterruptedException e){
                //Se não funcionar escreve isso no stackstrace
                e.printStackTrace();
            }
            //Mudar de activity no fim do splashscreen e destruir a activity para não voltarmos com o back button
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            SplashScreenActivity.this.finish();
        }
    }

}
