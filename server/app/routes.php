<?php

/*
  |--------------------------------------------------------------------------
  | Application Routes
  |--------------------------------------------------------------------------
  |
  | Here is where you can register all of the routes for an application.
  | It's a breeze. Simply tell Laravel the URIs it should respond to
  | and give it the Closure to execute when that URI is requested.
  |
 */

Route::get('/', function() {
    return View::make('hello');
});

Route::get('api/users/coordinates', 'HomeController@getUsersCoordinates');
Route::get('api/floor', 'HomeController@getFloorInfo');
Route::get('api/floors', 'HomeController@getFloors');
Route::get('api/test/floor', 'HomeController@testFloorInfo');
Route::get('api/test/users', 'HomeController@testUsersCoordinates');
