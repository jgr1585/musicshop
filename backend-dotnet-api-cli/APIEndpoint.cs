namespace FHVTeamD.MusicshopAPI.CLI
{
    class APIEndpoint
    {
        private Uri? _apiBaseUri;
        private string _apiPath;
        private string[] _requiredParams;
        private string[] _optionalParams;

        public APIEndpoint(Uri? apiBaseUri, string apiPath, string[] requiredParams, string[] optionalParams) {
            _apiBaseUri = apiBaseUri;
            _apiPath = apiPath;
            _requiredParams = requiredParams;
            _optionalParams = optionalParams;
        }

        public APIEndpoint(string apiPath, string[] requiredParams, string[] optionalParams) : this(null, apiPath, requiredParams, optionalParams) {

        }

        public APIEndpoint(string apiPath, string[] requiredParams) : this(apiPath, requiredParams, Array.Empty<string>()) {

        }

        public Uri ApiBaseUri {
            set { _apiBaseUri = value; }
        }

        public Uri ApiEndpointUri() {
            if (_apiBaseUri == null)
                throw (new InvalidOperationException("API base URI is not set for endpoint yet."));

            return new Uri(_apiBaseUri, _apiPath);
        }

        public string ApiPath {
            get { return _apiPath; }
        }

        public string[] RequiredParams {
            get { return _requiredParams; }
        }

        public string[] OptionalParams {
            get { return _optionalParams; }
        }
    }
}