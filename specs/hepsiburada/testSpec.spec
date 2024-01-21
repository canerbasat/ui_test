TestiniumCase
=====================


Testinium Case1
-----------------------------------------------------------
tags:stepbyStep
* Mevcut sayfanin "www.beymen.com" olması beklenir
* "btn_Accept_Cookies" elementi görünene kadar beklenir
* "btn_Accept_Cookies" elementine tıklanır
* "btn_Gender_Choose_Man" elementine tıklanır
* "tb_Search_Input" elementine excelden "0" satirdaki "0" sutundaki değer yazilir
* "btn_SearchArea_Clear" elementine tıklanır
* "btn_FocusedSearchArea_Input" elementine excelden "0" satirdaki "1" sutundaki değer yazilir
* "btn_FocusedSearchArea_Input" elementinde klavyeden enter gonderilir
* Mevcut sayfanin "beymen.com/search?q=g%C3%B6mlek" olması beklenir
* "btn_List_SearchPage_Products" elementi listeye atilir ve rastgele birine tiklanir
* "txt_Product_Brand" elementinin degeri txt dosyasına kaydedilir
* "txt_Product_Name" elementinin degeri txt dosyasına kaydedilir
* "txt_Product_Price" elementinin degeri txt dosyasına kaydedilir
* "txt_Product_Price" elementinin "text" degeri "priceAtProductPage" degiskenine kaydedilir
* "btn_List_Enabled_Variations" elementi listeye atilir ve rastgele birine tiklanir
* "btn_AddBasket" elementine tıklanır
* "btn_Cart" elementine js ile tıklanır
* "txt_Cart_ProductPrice" elementi görünene kadar beklenir
* "txt_Cart_ProductPrice" elementinin degeri txt dosyasına kaydedilir
* "txt_Cart_ProductPrice" elementinin "text" degeri "priceAtCartPage" degiskenine kaydedilir
* "priceAtProductPage" ve "priceAtCartPage" değişkenlerinin değerleri birbirini icerir
//* "sb_Product_Quantity" listesinden "2" degeri secilir ve adet degisikligi kontrol edilir
* "btn_Remove_Product_Cart" elementine tıklanır
* "txt_Empty_Cart" elementi görünene kadar beklenir
* "3" saniye bekle