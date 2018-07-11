package com.example.pedrolemos.livrosfinal.utils;

import com.example.pedrolemos.livrosfinal.R;
import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

public class MyWelcomeActivity extends WelcomeActivity {

    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.colorPrimaryDark)

                .page(new TitlePage(R.drawable.logo_vector_welcome,
                        "Welcome to Woodpecker!")
                )
                .page(new BasicPage(R.drawable.favorites_vector_welcome,
                        "Add favorites!",
                        "Personalize your account by adding your favorite books")
                )
                .page(new BasicPage(R.drawable.thumbup_vector_welcome,
                        "Recommendations",
                        "Get recommended new books based on your favorites")

                )
                .page(new BasicPage(R.drawable.account_vector_welcome,
                        "Account",
                        "You can check all your favorites and access the settings menu.")

                )
                .page(new BasicPage(R.drawable.account_vector_welcome,
                        "Now that you know...",
                        "Have fun using Woodpecker!")
                        .background(R.color.colorStart)

                )

                .swipeToDismiss(true)
                .exitAnimation(android.R.anim.fade_out)
                .build();
    }


}
