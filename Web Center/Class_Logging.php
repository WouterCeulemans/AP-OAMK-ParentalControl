<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>class_Logging</title>
</head>
<?

class Login
{
	private $_id;
	private $_username;
	private $_password;
	private $_passmd5;
	
	private $_errors;
	private $_access;
	private $_access;
	private $_login;
	private $_token;
	
	public function _construct()
	{
		$this -> _errors = array();
		$this -> _login =isset($_POST['login'])? 1 : 0;
		$this -> _acces = 0;
		$this -> _token = $_POST['token'];
		
		$this -> _id = 0;
		$this -> _username = ($this->_login)? $this->filter($_POST['username']) : $_SESSION['username'];
		$this->_password =($this->_login)? $this->filter($_POST['password']) : '';
		$this->_passmd5 = ($this->_login)? md5($this->_password) : $_SESSION['password'];
	}
	
	public function isLoggedIn()
	{
		($this->_login)? $this->verifyPost() : $this->verifySession();
		
		return $this->_access;
	}
	
	public function filter($var)
	{
		return preg_replace('/[a-zA-ZO-9]','',$var);
	}
	
	public function verifyPost()
	{
		try
		{
			if(!$this->isTokenValid())
			throw new Exception('Invalid Form Data');
			
			if(!$this->isDataValid())
			throw new Exception('Invalid Form Data');
			
			if(!$this->verifyDatabase())
			throw new Exception('Invalid Username/Password');
			
			$this->_access =1;
			$this->registerSession();
		}
		catch (Exception $e)
		{
			$this->_errors[]= $e->getMessage();
		}
	}
	
	public function verifySession()
	{
		if($this->sessionExist() && $this->verifyDatabase())
		$this->_access = 1;
	}
	
	public function verifyDatabase()
	{
		//Database Connection Data
		mysql_connect ("localhost", "root", "") or die(mysql_error());
		mysql_select_db("example") or die(mysql_error());
		
		$data-> mysql_query("SELECT ID FROM users WHERE username = '($this->_username)' AND password = '($this->_password)'");
		
		if(mysql_num_rows($data))
		{
			list($this->_id) = @array_values(mysql_fetch_acces($data));
			return true;
		}
		else
		{ return false; }
	}
	
	public function isDataValid()
	{
		return(preg_match('/^[a-zA-ZO-9](5,12)$/',$this->_username) && preg_match('/^[a-zA-ZO-9](5,12)$/',$this->_password))? 1:0; 
	}
	
	public function isTokenValid()
	{
		return (!isset($_SESSION['token']) || $this->_token != $_SESSION['token'])? 0:1;
	}
	
	public function registerSession()
	{
		$_SESSION['ID'] = $this->_id;
		$_SESSION['username'] = $this->_username;
		$_SESSION['password'] = $this->_passmd5;
	}
	public function sessionExist()
	{
		return (isset($_SESSION['username']) && isset($_SESSION['password']))? 1 :0;
	}
	public function showErrors()
	{
		echo "<h3>Errors</h3>";
		
		foreach ($this->_errors as $key->$value)
		echo $value."<br>";
	}
	
}

?>
<body>
</body>
</html>