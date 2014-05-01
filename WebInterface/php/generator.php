<?php
function GeneratePass ($length)
{
	$regex ="/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{5,13}$/";
	$chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-=~!@#$%^&*()_+,./<>?;:[]{}\|';
	do {
		$str = '';
		$max = strlen($chars) - 1;
		for ($i=0; $i < $length; $i++)
			$str .= $chars[mt_rand(0, $max)];
	} while (!preg_match ($regex, $str));
	return  "$str";
}

echo GeneratePass(10);
?>