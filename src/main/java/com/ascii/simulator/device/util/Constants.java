package com.ascii.simulator.device.util;

import org.json.JSONArray;

public class Constants {
    public static final String OPENAI_API_URL = "https://api.openai.com/v1";
    public static final String OPENAI_API_COMPLETION_PATH = OPENAI_API_URL + "/chat/completions";
    public static final String OPENAI_API_KEY = "ADD_SECRETE_KEY_HERE";
    public static final String OPENAI_API_MODEL = "gpt-4o";
    public static final double OPENAI_API_TEMPERATURE = 0.7;
    public static final int OPENAI_API_MAX_TOKENS = 150;
    public static final String DEVICES_DATA_DIR = "devices";
    public static final String TRAINING_DATA_DIR = "training_data";
    public static final String TRAINING_DATA_FILE = "training_data-%s.json";
    public static final String TRAINING_DATA_FILE_PATH = TRAINING_DATA_DIR + "/" + TRAINING_DATA_FILE;
    public static final String TRAINING_DATA_SEPARATOR = " ";
    public static final String TRAINING_DATA_FILE_FORMAT_REGEX_SPLIT_ESCAPE_NEWLINE = "\\n";
    public static final String PROMPT_DATA_BEFORE = "{'data': [{'role': 'system', 'content': \"You possess the knowledge of all the universe, answer any question given to you truthfully and to your fullest ability. \\nYou are also a smart home manager who has been given permission to control my smart home which is powered by Home Assistant.\\nI will provide you information about my smart home along, you can truthfully make corrections or respond in a friendly manner.\\nYou will talk as if you are making a friendly conversation.\\nYou will talk using complete sentences, not point form.\\n\\n\\nCurrent Local Time: %s\\nDay of week: %s\\n\\nRequest Area: %s\\n\\nAvailable Devices:\\n```csv\\n";
    public static final String PROMPT_DATA_AFTER = "\\n\\nThe current state of devices is provided in Available Devices.\\n\\nOnly use the execute_services function when smart home actions are requested.\\nDo not tell me what you're thinking about doing either, just do it.\\nIf I ask you about the current state of the home, or many devices I have, or how many devices are in a specific state, just respond with the accurate information but do not call the execute_services function.\\nIf an function_call is to be returned, and we are running a script, the service value should be turn_on\\nIf I ask you what time or date it is be sure to respond in a human readable format.\\nIf I ask you about weather, run the weather.get_forecasts on the appropriate entity, set service_data.type to hourly or daily depends on request context.\\nIf I ask you about upcoming events, run calendar.get_events. Set service_data.entity_id to the appropriate entity, set service_data.start_date_time and service_data.end_date_time to the right value depends on request context.\\nTo start a timer, pick a time that is currently idle, and include service_data.duration in the function call\\nYou can try to guess the intend of the user, for example: if user say it's really dark, you will turn on the light.\\nYou can look at the Request Area to determine which device the user wants to control. For example, if the user wants to turn on the light and Request Area is office, then he is probably talking about the office light.\\nIf you don't have enough information to execute a smart home command then specify what other information you need.\"}]}";
    public static final String FUNCTIONS_DATA = "[{'name': 'execute_services', 'description': 'Use this function to execute service of devices in Home Assistant.', 'parameters': {'type': 'object', 'properties': {'list': {'type': 'array', 'items': {'type': 'object', 'properties': {'domain': {'type': 'string', 'description': 'The domain of the service'}, 'service': {'type': 'string', 'description': 'The service to be called'}, 'service_data': {'type': 'object', 'description': 'The service data object to indicate what to control.', 'properties': {'entity_id': {'type': 'string', 'description': 'The entity_id retrieved from available devices. It must start with domain, followed by dot character.'}, 'summary': {'type': 'string', 'description': 'Optional field when adding calendar events.'}}, 'required': ['entity_id']}}, 'required': ['domain', 'service', 'service_data']}}}}}]";
    public static final JSONArray FUNCTIONS_DATA_JSON = new JSONArray(FUNCTIONS_DATA);
}
