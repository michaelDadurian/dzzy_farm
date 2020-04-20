"""Module with utility functions"""
from configparser import ConfigParser
import sys

def get_index(input_string, sub_string, ordinal):
    current = -1
    for i in range(ordinal):
        current = input_string.index(sub_string, current + 1)
    return current


def get_settings_variables():
    """Reads settings from settings.ini and returns them"""
    config = ConfigParser()
    try:
        config.read('settings/settings.ini')
    except FileNotFoundError:
        sys.exit("settings.ini file not found. "
                 "Make sure it's in the same directory.")
    else:
    	# Return our [USER_SETTINGS]
        use_proxies = config['USER_SETTINGS'].getboolean('use_proxies')
        proxy_auth_type = config['USER_SETTINGS'].getint('proxy_auth_type')
        captcha_api_key = config['USER_SETTINGS'].get('2captcha_api_key')
        num_of_accs = config['USER_SETTINGS'].getint('num_of_accs')
        username_prefix = config['USER_SETTINGS'].get('username_prefix')
        password = config['USER_SETTINGS'].get('password')
        acc_details_format = config['USER_SETTINGS'].getboolean('acc_details_format')


         # Return our [SITE_SETTINGS]
        site_key = config['SITE_SETTINGS'].get('site_key')
        site_url = config['SITE_SETTINGS'].get('site_url')

        # Return our [TRIBOT_CLI_SETTINGS]
        use_tribot = config['TRIBOT_CLI_SETTINGS'].getboolean('use_tribot')
        tribot_username = config['TRIBOT_CLI_SETTINGS'].get('tribot_username')
        tribot_password = config['TRIBOT_CLI_SETTINGS'].get('tribot_password')
        tribot_script = config['TRIBOT_CLI_SETTINGS'].get('tribot_script')
        tribot_script_args = config['TRIBOT_CLI_SETTINGS'].get('script_args')

        # Return our [OSBOT_CLI_SETTINGS]
        use_osbot = config['OSBOT_CLI_SETTINGS'].getboolean('use_osbot')
        osbot_username = config['OSBOT_CLI_SETTINGS'].get('osbot_username')
        osbot_passord = config['OSBOT_CLI_SETTINGS'].get('osbot_password')
        osbot_script = config['OSBOT_CLI_SETTINGS'].get('osbot_script')
        osbot_script_args = config['OSBOT_CLI_SETTINGS'].get('script_args')



        return (use_proxies, captcha_api_key, num_of_accs, site_key, site_url,
                username_prefix, password, acc_details_format,
                use_tribot, tribot_username, tribot_password, tribot_script, tribot_script_args,
                proxy_auth_type,
                use_osbot, osbot_username, osbot_passord, osbot_script, osbot_script_args)
