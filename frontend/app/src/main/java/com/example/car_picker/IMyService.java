package com.example.car_picker;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IMyService {
     @POST("customer")
     @FormUrlEncoded
    Observable<String>registerUser(@Field("First_Name") String First_Name,
                                   @Field("Last_Name") String Last_Name,
                                   @Field("NIC_Passport_No") String NIC_Passport_No,
                                   @Field("DateOfBirth") String DateOfBirth,
                                   @Field("Deposit_Amount") int Deposit_Amount,
                                   @Field("Password") String Password,
                                   @Field("Email_Address") String Email_Address,
                                   @Field("Mobile") String Mobile);
     @POST("customer/login/NIC_Passport_No")
    @FormUrlEncoded
    Observable<String>loginUser(@Field("NIC_Passport_No") String NIC_Passport_No,
                                   @Field("Password") String Password);
}
