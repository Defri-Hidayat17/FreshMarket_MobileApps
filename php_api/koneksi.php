<?php
$host = "localhost";
$user = "root"; // default XAMPP user
$pass = "";     // default XAMPP password kosong
$db   = "freshmarket";

// Gunakan nama variabel $koneksi (bukan $conn)
$koneksi = new mysqli($host, $user, $pass, $db);

if ($koneksi->connect_error) {
    die("Connection failed: " . $koneksi->connect_error);
}
?>
