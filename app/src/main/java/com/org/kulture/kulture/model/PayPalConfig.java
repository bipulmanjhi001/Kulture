package com.org.kulture.kulture.model;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;

/**
 * Created by tkru on 9/18/2017.
 */

public class PayPalConfig {

    // PayPal app configuration
    public static final String PAYPAL_CLIENT_ID = "AYWiyrGb36ItSWui8g2BNNCrTQcT_l7XtA5LYjLCY4iCzPVwmxvyDAOgM6Qm6nCCcgXX5r8otUF6b7I1";
    //public static final String PAYPAL_CLIENT_SECRET = "EFkA7gPlQhqrK47R58yZklF5z697ZtqIMVfQycA6_RqNQQZvfr2xrvsgxtbEgx_tpPjFpo0b5r2LWfRT";

    public static final String PAYPAL_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    public static final String PAYMENT_INTENT = PayPalPayment.PAYMENT_INTENT_SALE;
    public static final String DEFAULT_CURRENCY = "USD";
}
