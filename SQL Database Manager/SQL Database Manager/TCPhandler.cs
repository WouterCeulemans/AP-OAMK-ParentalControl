using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Text;
using MySql.Data.MySqlClient;
using Newtonsoft.Json;

namespace SQL_Database_Manager
{
    class ClientHandler
    {
        private readonly TcpClient _clientSocket;
        private const string Db = @"server=192.168.0.106;userid=root;password=bx7QBSruC;database=parentcontrol";
        MySqlConnection _conn;

        public ClientHandler (TcpClient clientSocket)
        {
            _clientSocket = clientSocket;
            _clientSocket.ReceiveTimeout = 100;
        }

        public void Process (Object o)
        {
            try
            {
                if (_clientSocket.Available <= 0) return;
                var networkStream = _clientSocket.GetStream ();
                var bytes         = new byte[_clientSocket.ReceiveBufferSize];
                var bytesRead     = networkStream.Read (bytes, 0, _clientSocket.ReceiveBufferSize);
                if (bytesRead <= 0) return;
                var data          = Encoding.UTF8.GetString (bytes, 0, bytesRead).Split (';');
                Console.WriteLine (Encoding.UTF8.GetString (bytes, 0, bytesRead));
                switch (data [0])
                {
                    case "get"   :
                    {
                        var root = new Rootobject {Device = new [] {Get (data [1])}};
                        var _string = JsonConvert.SerializeObject (root);
                        var sendBytes = Encoding.UTF8.GetBytes(_string);
                        networkStream.Write(sendBytes, 0, sendBytes.Length);
                        break;
                    }
                    case "create":
                    {
                        Create (data);
                        break;
                    }
                    case "push"  : 
                    {
                        var device = (JsonConvert.DeserializeObject<Rootobject> (data[1])).Device[0];
                        Push (device);
                        break;
                    }
                }

                networkStream.Close ();
                _clientSocket.Close ();
                Console.WriteLine      ("{0} | Info | User Disconnected", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                Program.File.WriteLine ("{0} | Info | User Disconnected", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                Program.File.Flush ();
            }
            catch (Exception)
            {
                _clientSocket.Close ();
                Console.WriteLine      ("{0} | Error | User Lost Connection", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                Program.File.WriteLine ("{0} | Error | User Lost Connection", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                Program.File.Flush ();
            }
        }

        private void Create (IList<string> data)
        {
            _conn = new MySqlConnection (Db);
            _conn.Open ();

            var query = String.Format ("insert into users (ID,Name) Values(\"{0}\",\"{1}\");", data [1], data [2]);
            try
            {
                var cmd = new MySqlCommand (query, _conn);
                cmd.ExecuteScalar ();
            }
            catch (Exception)
            {
                Console.WriteLine      ("{0} | Error | SQL Error", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                Program.File.WriteLine ("{0} | Error | SQL Error", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                Program.File.Flush ();
            }
            _conn.Close ();
        }

        private void Push (Device device)
        {
            _conn = new MySqlConnection (Db);
            _conn.Open ();
            foreach (var app in device.Apps)
            {
                var query = String.Format ("insert into apps (ID,PackageName,Name,Visible) Values(\"{0}\",\"{1}\",\"{2}\",\"{3}\") on duplicate key update Name=\"{2}\", Visible=\"{3}\";",
                                           device.DeviceId, app.PackageName, app.Name, app.Visible);
                try
                {
                    var cmd = new MySqlCommand (query, _conn);
                    cmd.ExecuteScalar ();
                }
                catch (Exception)
                {
                    Console.WriteLine      ("{0} | Error | SQL Error", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                    Program.File.WriteLine ("{0} | Error | SQL Error", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                    Program.File.Flush ();
                    _conn.Close ();
                    break;
                }
            }
            foreach (var contact in device.Contacts)
            {
                var query = String.Format ("insert into contacts (ID,Contact_ID,SurName,Name,Number,TxtAmount,TxtMax,CallAmount,CallMax) Values(\"{0}\",\"{1}\",\"{2}\",\"{3}\",\"{4}\",\"{5}\",\"{6}\",\"{7}\",\"{8}\") on duplicate key update SurName=\"{2}\",Name=\"{3}\",Number=\"{4}\",TxtAmount=\"{5}\",TxtMax=\"{6}\",CallAmount=\"{7}\",CallMax=\"{8}\";",
                                           device.DeviceId, contact.ContactId, contact.SurName, contact.Name, contact.Number, contact.TxtAmount, contact.TxtMax, contact.CallAmount, contact.CallMax);
                try
                {
                    var cmd = new MySqlCommand (query, _conn);
                    cmd.ExecuteScalar ();
                }
                catch (Exception)
                {
                    Console.WriteLine      ("{0} | Error | SQL Error", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                    Program.File.WriteLine ("{0} | Error | SQL Error", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                    Program.File.Flush ();
                    _conn.Close ();
                    break;
                }
            }
            foreach (var coordinate in device.Locations)
            {
                var query = String.Format ("insert into coordinates (ID,Pos_ID,Latitude,Longitude) Values(\"{0}\",\"{1}\",\"{2}\",\"{3}\") on duplicate key update Latitude=\"{2}\",Longitude=\"{3}\";",
                                           device.DeviceId, coordinate.PosId, coordinate.Lat, coordinate.Long);
                try
                {
                    var cmd = new MySqlCommand (query, _conn);
                    cmd.ExecuteScalar ();
                }
                catch (Exception)
                {
                    Console.WriteLine      ("{0} | Error | SQL Error", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                    Program.File.WriteLine ("{0} | Error | SQL Error", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                    Program.File.Flush ();
                    _conn.Close ();
                    break;
                }
            }
            _conn.Close ();
        }

        private Device Get (string id)
        {
            var device       = new Device
            {
                DeviceId = id,
                Apps = GetAppList (id),
                Contacts = GetContactList (id),
                Locations = GetLocationList (id)
            };
            return device;
        }

        private App      [] GetAppList      (string id) 
        {
            var query = String.Format ("select packagename,name,visible from apps where id=\"{0}\";", id);
            _conn = new MySqlConnection (Db);
            _conn.Open  ();
            try
            {
                var cmd     = new MySqlCommand  (query, _conn);
                var obj     = cmd.ExecuteReader ();
                var appList = new List<App>     ();
                while (obj.Read ())
                {
                    appList.Add (new App
                    {
                        PackageName = obj.GetString (0),
                        Name        = obj.GetString (1),
                        Visible     = obj.GetString (2)
                    });
                }
                _conn.Close ();
                return appList.ToArray ();
            }
            catch (Exception)
            {
                Console.WriteLine      ("{0} | Error | SQL Error", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                Program.File.WriteLine ("{0} | Error | SQL Error", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                Program.File.Flush ();
                _conn.Close ();
                return new App[0];
            }
        }
        private Contact  [] GetContactList  (string id) 
        {
            var query = String.Format ("select Contact_ID,SurName,Name,Number,TxtAmount,TxtMax,CallAmount,CallMax from contacts where id=\"{0}\";", id);
            _conn = new MySqlConnection (Db);
            _conn.Open ();
            try
            {
                var cmd         = new MySqlCommand  (query, _conn);
                var obj         = cmd.ExecuteReader ();
                var contactList = new List<Contact> ();
                while (obj.Read ())
                {
                    contactList.Add (new Contact
                    {
                        ContactId  = obj.GetString (0),
                        SurName    = obj.GetString (1),
                        Name       = obj.GetString (2),
                        Number     = obj.GetString (3),
                        TxtAmount  = obj.GetString (4),
                        TxtMax     = obj.GetString (5),
                        CallAmount = obj.GetString (6),
                        CallMax    = obj.GetString (7),
                    });
                }
                _conn.Close ();
                return contactList.ToArray ();
            }
            catch (Exception)
            {
                Console.WriteLine      ("{0} | Error | SQL Error", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                Program.File.WriteLine ("{0} | Error | SQL Error", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                Program.File.Flush ();
                _conn.Close ();
                return new Contact[0];
            }
        }
        private Location [] GetLocationList (string id) 
        {
            var query = String.Format ("select Pos_ID,Latitude,Longitude from coordinates where id=\"{0}\";", id);
            _conn = new MySqlConnection (Db);
            _conn.Open ();
            try
            {
                var cmd          = new MySqlCommand   (query, _conn);
                var obj          = cmd.ExecuteReader  ();
                var locationList = new List<Location> ();
                while (obj.Read ())
                {
                    locationList.Add (new Location
                    {
                        PosId = obj.GetString (0),
                        Lat   = obj.GetString (1),
                        Long  = obj.GetString (2)
                    });
                }
                _conn.Close ();
                return locationList.ToArray ();
            }
            catch (Exception)
            {
                Console.WriteLine      ("{0} | Error | SQL Error", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                Program.File.WriteLine ("{0} | Error | SQL Error", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                Program.File.Flush ();
                _conn.Close ();
                return new Location[0];
            }
        }
    }
}
