using Newtonsoft.Json;

namespace FHVTeamD.MusicshopAPI.CLI
{
    internal class Program
    {
        private List<APIEndpoint> _apiEndpoints = new List<APIEndpoint>() {
            new APIEndpoint("article/search", new string[0], new string[] {"title", "artist"})
        };
        private string _banner = @" __    __     __  __     ______     __     ______     ______     __  __     ______     ______" + Environment.NewLine + 
                                @"/\ " + "\"" + @"-./  \   /\ \/\ \   /\  ___\   /\ \   /\  ___\   /\  ___\   /\ \_\ \   /\  __ \   /\  == \" + Environment.NewLine +   
                                @"\ \ \-./\ \  \ \ \_\ \  \ \___  \  \ \ \  \ \ \____  \ \___  \  \ \  __ \  \ \ \/\ \  \ \  _-/" + Environment.NewLine +  
                                @" \ \_\ \ \_\  \ \_____\  \/\_____\  \ \_\  \ \_____\  \/\_____\  \ \_\ \_\  \ \_____\  \ \_\" + Environment.NewLine +    
                                @"  \/_/  \/_/   \/_____/   \/_____/   \/_/   \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/" + Environment.NewLine +    
                                Environment.NewLine +                                                                     
                                @" ______     ______   __        ______     __         __" + Environment.NewLine +                                         
                                @"/\  __ \   /\  == \ /\ \      /\  ___\   /\ \       /\ \" + Environment.NewLine +                                        
                                @"\ \  __ \  \ \  _-/ \ \ \     \ \ \____  \ \ \____  \ \ \" + Environment.NewLine +                                       
                                @" \ \_\ \_\  \ \_\    \ \_\     \ \_____\  \ \_____\  \ \_\" + Environment.NewLine +                                      
                                @"  \/_/\/_/   \/_/     \/_/      \/_____/   \/_____/   \/_/";

        public Program() {
            // welcome message
            Console.WriteLine(_banner);
            Console.WriteLine();
            Console.WriteLine("Welcome to Musicshop Dotnet API Command Line Interface!");

            // input of musicshop API URI
            Uri? apiBaseUri = null;
            while (apiBaseUri == null) {
                Console.WriteLine();
                Console.WriteLine("Please enter the base URI to the musicshop API:");

                string? crIn;
                while (string.IsNullOrEmpty(crIn = Console.ReadLine()));
                Uri? uri;
                if (Uri.TryCreate(crIn.EndsWith('/') ? crIn : crIn+'/', UriKind.Absolute, out uri)
                    && (uri.Scheme == Uri.UriSchemeHttp || uri.Scheme == Uri.UriSchemeHttps))
                    apiBaseUri = uri;
                else
                    Console.WriteLine("Invalid URI: The format of the URI could not be determined or is not supported by this API CLI. Please try again!");
            }
            _apiEndpoints.ForEach(e => e.ApiBaseUri = apiBaseUri);

            while (true) {
                // selection of API endpoint path by list of available endpoint paths with their query params
                Console.WriteLine();
                Console.WriteLine("Please select an API endpoint path to request by entering its number:");
                int i = 0;
                foreach (var apiEndpoint in _apiEndpoints) {
                    string requiredParams = String.Join(" [required], ", apiEndpoint.RequiredParams);
                    if (requiredParams.Length != 0)
                        requiredParams += " [required]";
                    string optionalParams = String.Join(", ", apiEndpoint.OptionalParams);
                    string paramsInfo;
                    if (requiredParams.Length != 0 && optionalParams.Length != 0) 
                        paramsInfo = String.Join(", ", new string[] {requiredParams, optionalParams});
                    else
                        paramsInfo = requiredParams.Length != 0 ? requiredParams : optionalParams;
                    if (paramsInfo.Length != 0)
                        paramsInfo = " (query params: " + paramsInfo + ")";
                    Console.WriteLine(++i + ".) \"" + apiEndpoint.ApiPath + "\"" + paramsInfo);
                }

                APIEndpoint? selectedApiEndpoint = null;
                while (selectedApiEndpoint == null) {
                    string? selectedIndex = Console.ReadLine();
                    int selectedIndexNo;
                    Int32.TryParse(selectedIndex, out selectedIndexNo);

                    if (selectedIndexNo > 0 && selectedIndexNo <= _apiEndpoints.Count)
                        selectedApiEndpoint = _apiEndpoints.ElementAt(selectedIndexNo - 1);
                }

                // input of params for selected API endpoint
                Console.WriteLine();
                Console.WriteLine("Please proceed by entering the API params:");
                Dictionary<string, string> queryParams = new Dictionary<string, string>();
                foreach (var reqParam in selectedApiEndpoint.RequiredParams) {
                    Console.WriteLine("Param \"" + reqParam + "\" [required]: ");
                    string? param;
                    while (string.IsNullOrEmpty(param = Console.ReadLine()));
                    queryParams.Add(reqParam, param);
                }
                foreach (var optParam in selectedApiEndpoint.OptionalParams) {
                    Console.WriteLine("Param \"" + optParam + "\": ");
                    string? param = Console.ReadLine();
                    if (!string.IsNullOrEmpty(param))
                        queryParams.Add(optParam, param);
                }

                // send API request and output result
                Console.WriteLine();
                ApiHttpRequest apiHttpClient = new ApiHttpRequest(selectedApiEndpoint, queryParams);
                Console.WriteLine("Sending API request to \"" + selectedApiEndpoint.ApiEndpointUri() + "\"..");
                try {
                    Task<string> task = apiHttpClient.send();
                    task.Wait();

                    Console.WriteLine("API response:");
                    string json = JsonConvert.SerializeObject(JsonConvert.DeserializeObject(task.Result), Formatting.Indented);
                    Console.WriteLine(json);
                } catch (Exception e) {
                    Console.WriteLine(e.Message);
                }
            }
        }

        static void Main(string[] args)
        {
            new Program();
        }
    }
}