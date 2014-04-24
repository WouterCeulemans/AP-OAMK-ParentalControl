using MySql.Data.MySqlClient;
using System;
using System.INI;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Threading;

namespace SQL_Database_Manager
{
    internal class Program
    {
        internal static StreamWriter LogFile;
        internal static string       Db;

        private  static int          _portNum;
        private  static string       _databaseName;
        private  static INIFile      _ini;
        private  static IPAddress    _ip;
        private  static bool         _continue = true;
        private static void Main ( )                                
        {
            Init ();
            TestDatabaseConnection ();

            var listener = new TcpListener (_ip, _portNum);
            try                 
            {
                listener.Start ();
                Console.WriteLine ("Waiting for connection.");
                for (;;)
                {
                    var handler = listener.AcceptTcpClient ();
                    WriteInfo (String.Format ("{0} | Info | Client Connected", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                    var client  = new ClientHandler (handler);
                    ThreadPool.QueueUserWorkItem (client.Process);
                }
            }
            catch (Exception e) 
            {
                WriteInfo (String.Format ("{0} | Error | {1} ", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"), e));
            }
        }

        private static void Init                    ( )             
        {
            if (!Directory.Exists ("./config"))
                Directory.CreateDirectory ("./config");
            if (!Directory.Exists ("./logs"))
                Directory.CreateDirectory ("./logs");
            _ini = new INIFile ("./config/config.ini");
            LogFile = new StreamWriter ("./logs/SQLiteServer_" + DateTime.Now.ToString ("yyyy-MM-d_HH_mm_ss") + ".log");
            
            if (!File.Exists ("./config/config.ini"))
            {
                WriteInfo (String.Format ("{0} | Error | config.ini not found; Continuing with default values and writing default config file", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                _portNum = 8080;
                _ip = IPAddress.Parse ("0.0.0.0");
                Db  = String.Format   ("server={0};user={1};password={2};database={3}", "localhost", "root", "", "ParentalControlDb");
                _ini.SetValue ("Server"  , "Port"        , "8080"             );
                _ini.SetValue ("Server"  , "ListeningIP" , "0.0.0.0"          );
                _ini.SetValue ("Database", "Address"     , "localhost"        );
                _ini.SetValue ("Database", "User"        , "root"             );
                _ini.SetValue ("Database", "Password"    , ""                 );
                _ini.SetValue ("Database", "DatabaseName", "ParentalControlDb");
            }
            else
            {
                try   
                {
                    _portNum = int.Parse (_ini.GetValue ("Server", "Port"));
                }
                catch 
                {
                    _portNum = 8080;
                    WriteInfo (String.Format ("{0} | Error | Error in config file fallback to port 8080 ", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                }
                try   
                {
                    _ip = IPAddress.Parse (_ini.GetValue ("Server", "ListeningIP"));
                }
                catch 
                {
                    _ip = IPAddress.Parse ("0.0.0.0");
                    WriteInfo (String.Format ("{0} | Error | Error in config file fallback to 0.0.0.0 ", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                }

                var host      = "localhost";
                var user      = "root";
                var password  = "";
                _databaseName = "ParentalControlDb";

                try   
                {
                    host =  _ini.GetValue ("DataBase", "Address");
                }
                catch 
                {
                    WriteInfo (String.Format ("{0} | Error | Error in config file fallback to localhost ", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                }
                try   
                {
                    user =  _ini.GetValue ("DataBase", "User");
                }
                catch 
                {
                    WriteInfo (String.Format ("{0} | Error | Error in config file fallback to root ", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                }
                try   
                {
                    password =  _ini.GetValue ("DataBase", "Password");
                }
                catch 
                {
                    WriteInfo (String.Format ("{0} | Error | Error in config file fallback to empty password ", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                }
                try   
                {
                    _databaseName =  _ini.GetValue ("DataBase", "DatabaseName");
                }
                catch 
                {
                    WriteInfo (String.Format ("{0} | Error | Error in config file fallback to ParentalControlDb ", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                }
                Db = String.Format ("server={0};user={1};password={2};database={3}",
                                       host, user, password, _databaseName);
            }
        }

        private static void TestDatabaseConnection  ( )             
        {
            var db   = Db.Substring (0, Db.LastIndexOf (';'));
            var conn = new MySqlConnection (db);
            try                         
            {
                conn.Open ();
            }
            catch (MySqlException e)    
            {
                switch (e.Number)
                {
                    case 1042:
                        WriteInfo (String.Format ("{0} | FATAL | Can't connect to mySQL host.",
                                                  DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                        break;
                    case 0:
                        WriteInfo (String.Format ("{0} | FATAL | Authentication failed: Access denied.",
                                                  DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                        break;
                    default:
                        throw;
                }
                _continue = false;
            }
            finally                     
            {
                conn.Close ();
            }
            if (!_continue) Environment.Exit (-1);

            try
            {
                conn.Open ();
                var query = String.Format ("SHOW DATABASES LIKE '{0}';", _databaseName);
                var cmd = new MySqlCommand (query, conn);
                var obj = cmd.ExecuteScalar ();
                conn.Close ();

                if (obj != null) return;

                WriteInfo (String.Format ("{0} | Info | Database does not exist. Creating database.",
                                          DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                conn.Open ();
                query = String.Format ("create database {0}", _databaseName);
                cmd = new MySqlCommand (query, conn);
                cmd.ExecuteScalar ();
                conn.Close ();
                conn = new MySqlConnection (Db);
                conn.Open ();
                query = File.ReadAllText ("./SQL/Create_Database.sql");
                cmd = new MySqlCommand (query, conn);
                cmd.ExecuteScalar ();
            }
            finally
            {
                conn.Close ();
            }
        }

        internal static void WriteInfo              (string info)   
        {
            Console.WriteLine (info);
            LogFile.WriteLine    (info);
            LogFile.Flush ();
        }
    }
}
