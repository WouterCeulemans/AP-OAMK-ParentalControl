using System.Threading;
using MySql.Data.MySqlClient;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Text;

namespace SQL_Database_Manager
{
    class ClientHandler
    {
        private readonly string    _db = Program.Db;
        private readonly TcpClient _clientSocket;
        private MySqlConnection    _conn;


        public ClientHandler            (TcpClient clientSocket) 
        {
            _clientSocket = clientSocket;
            _clientSocket.ReceiveTimeout = 100;
        }

        public void         Process         (Object o)           
        {
            try
            {
                Thread.Sleep (2000);
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
                        var root      = new Rootobject {Device = new [] {Get (data [1])}};
                        var _string   = JsonConvert.SerializeObject (root);
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
                        var device = (JsonConvert.DeserializeObject<Rootobject> (data [1])).Device[0];
                        Push (device);
                        break;
                    }
                }

                networkStream.Close ();
                _clientSocket.Close ();
                Program.WriteInfo (String.Format ("{0} | Info | User Disconnected", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
            }
            catch (Exception)
            {
                _clientSocket.Close ();
                Program.WriteInfo (String.Format ("{0} | Error | User Lost Connection", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
            }
        }

        private void        Create          (IList<string> data) 
        {
            _conn = new MySqlConnection (_db);
            var query = String.Format ("insert into devices (ID,Name) Values(\"{0}\",\"{1}\");", data [1], data [2]);
            try
            {
                _conn.Open ();
                var cmd = new MySqlCommand (query, _conn);
                cmd.ExecuteScalar ();
            }
            catch (Exception e)
            {
                Program.WriteInfo (String.Format ("{0} | Error | SQL Error: {1}", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"), e.Message));
            }
            _conn.Close ();
        }

        private void        Push            (Device device)      
        {
            _conn = new MySqlConnection (_db);
            try
            {
                _conn.Open ();
                foreach (var app in device.Apps)
                {
                    var query = String.Format ("insert into apps (ID,PackageName,Name,Visible) Values(\"{0}\",\"{1}\",\"{2}\",\"{3}\") on duplicate key update Name=\"{2}\", Visible=\"{3}\";",
                                               device.DeviceId, app.PackageName, app.Title, app.Visible);

                    var cmd = new MySqlCommand (query, _conn);
                    cmd.ExecuteScalar ();
                }
                foreach (var contact in device.Contacts)
                {
                    var query = String.Format ("insert into contacts (ID,Contact_ID,SurName,Name,Number,TxtAmount,TxtMax,CallAmount,CallMax) Values(\"{0}\",\"{1}\",\"{2}\",\"{3}\",\"{4}\",\"{5}\",\"{6}\",\"{7}\",\"{8}\") on duplicate key update SurName=\"{2}\",Name=\"{3}\",Number=\"{4}\",TxtAmount=\"{5}\",TxtMax=\"{6}\",CallAmount=\"{7}\",CallMax=\"{8}\";",
                                               device.DeviceId, contact.ContactId, contact.SurName, contact.Name, contact.Number, contact.TxtAmount, contact.TxtMax, contact.CallAmount, contact.CallMax);
                    var cmd = new MySqlCommand (query, _conn);
                    cmd.ExecuteScalar ();
                }
                foreach (var coordinate in device.Locations)
                {
                    var query =
                        String.Format ("insert into coordinates (ID,Pos_ID,Latitude,Longitude) Values(\"{0}\",\"{1}\",\"{2}\",\"{3}\") on duplicate key update Latitude=\"{2}\",Longitude=\"{3}\";",
                                       device.DeviceId, coordinate.PosId, coordinate.Latitude, coordinate.Longitude);
                    var cmd = new MySqlCommand (query, _conn);
                    cmd.ExecuteScalar ();
                }

            }
            catch (Exception e)
            {
                Program.WriteInfo (String.Format ("{0} | Error | SQL Error: {1}", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"), e.Message));
            }
            _conn.Close ();
        }

        private Device      Get             (string id)          
        {
            var device       = new Device
            {
                DeviceId = id,
                Apps = GetAppList (id),
                Contacts = GetContactList (id)
            };
            return device;
        }

        private App      [] GetAppList      (string id)          
        {
            var query = String.Format ("select packagename,name,visible from apps where id=\"{0}\";", id);
            _conn = new MySqlConnection (_db);
            try
            {
                _conn.Open ();
                var cmd     = new MySqlCommand  (query, _conn);
                var obj     = cmd.ExecuteReader ();
                var appList = new List<App>     ();
                while (obj.Read ())
                {
                    appList.Add (new App
                    {
                        PackageName = obj.GetString (0),
                        Title       = obj.GetString (1),
                        Visible     = obj.GetString (2)
                    });
                }
                _conn.Close ();
                return appList.ToArray ();
            }
            catch (Exception e)
            {
                Program.WriteInfo (String.Format ("{0} | Error | SQL Error: {1}", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"), e.Message));
                _conn.Close ();
                return new App[0];
            }
        }
        private Contact  [] GetContactList  (string id)          
        {
            var query = String.Format ("select Contact_ID,SurName,Name,Number,TxtAmount,TxtMax,CallAmount,CallMax from contacts where id=\"{0}\";", id);
            _conn = new MySqlConnection (_db);
            try
            {
                _conn.Open ();
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
            catch (Exception e)
            {
                Program.WriteInfo (String.Format ("{0} | Error | SQL Error: {1}", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"), e.Message));
                Program.LogFile.Flush ();
                _conn.Close ();
                return new Contact[0];
            }
        }
    }
}
