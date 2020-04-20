"""Creates accounts and runs modules based on settings.ini"""
#!/usr/bin/env python3

import random
import string
import sys
from time import sleep
from socket import error as socket_error
import requests
from modules.bot_client_cli.tribot_cli import use_tribot
from modules.bot_client_cli.osbot_cli import use_osbot

try:
    from modules.helper_modules.utility import get_settings_variables, get_index
except ImportError as error:
    print(error)


HEADERS = {
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5)'
                  ' AppleWebKit/537.36 (KHTML, like Gecko)'
                  ' Chrome/58.0.3029.110 Safari/537.36'}
try:
    PROXY_LIST = open("settings/proxy_list.txt", "r")
except FileNotFoundError:
    sys.exit("proxy_list.txt wasn't found. "
             "Make sure it's in the same directory.")

# Settings pulled from get_settings_variables -> settings.ini file
USE_PROXIES = get_settings_variables()[0]
NUM_OF_ACCS = get_settings_variables()[2]
SITE_URL = get_settings_variables()[4]
TRIBOT_ACTIVE = get_settings_variables()[8]
OSBOT_ACTIVE = get_settings_variables()[14]



def get_ip() -> str:
    """
    Gets the user's external IP that will be used to create the account.
    Because of external dependency, we have a backup site to pull from if needed
    """
    users_ip = requests.get('https://api.ipify.org').text
    if not users_ip:
        users_ip = requests.get('http://ip.42.pl/raw').text
    return users_ip



def get_proxy() -> dict:
    """
    Returns our next proxy to use from the proxy_list.txt file.
    If we run out of proxies before we make all of the accounts, return to top.
    """
    try:
        proxy = {"https": (next(PROXY_LIST))}

        return proxy
    except StopIteration:
        # We'll return to the top of our list once we run out of proxies
        PROXY_LIST.seek(0)
        proxy = {"https": (next(PROXY_LIST))}
        return proxy



def access_page(proxy=None):
    """
    Sends a get request to our account creation url

    params:
    proxy (str): Proxy to use for get request (Default=None)

    returns:
    bool: True if the get request succeeds, false otherwise.
    """
    print(proxy)
    if USE_PROXIES:
        try:
            response = requests.get(SITE_URL, proxies=proxy, headers=HEADERS)
        except socket_error as error:
            print(error)
            print(f"Something with your proxy: {proxy} is likely bad.")
    else:
        response = requests.get(SITE_URL, headers=HEADERS)

    if response.ok:
        print("Loaded page successfully. Continuing.")
        return True
    else:
        print(f"Failed to load page. Status code: {response}")
        return False


def captcha_solver():
    """Handles and returns recaptcha answer for osrs account creation page"""
    site_key = get_settings_variables()[3]  # osrs site key
    api_key = get_settings_variables()[1]  # api key read from settings.ini
    if not api_key:
        raise ValueError("No API key was found in settings.ini.")


    s = requests.Session()

    # here we post and parse site key to 2captcha to get captcha ID
    try:
        captcha_id = s.post(f"http://2captcha.com/in.php?key={api_key}"
                            f"&method=userrecaptcha&googlekey={site_key}"
                            f"&pageurl={SITE_URL}").text.split('|')[1]
    except IndexError:
        print("You likely don't have a valid 2captcha.com API key with funds"
              " in your settings.ini file. Fix and re-run the program.")

    # then we parse gresponse from 2captcha response
    recaptcha_answer = s.get(
        f"http://2captcha.com/res.php?key={api_key}"
        f"&action=get&id={captcha_id}").text
    print("Solving captcha...")
    while 'CAPCHA_NOT_READY' in recaptcha_answer:
        sleep(6)
        recaptcha_answer = s.get(
            f"http://2captcha.com/res.php?key={api_key}"
            f"&action=get&id={captcha_id}").text
    try:
        recaptcha_answer = recaptcha_answer.split('|')[1]
    except IndexError:
        print("2captcha failed to solve this one.. Returning a blank response "
              "If the program fails to continue, please msg Gavin with error.")
        recaptcha_answer = ''
    else:
        return recaptcha_answer


def get_payload(captcha) -> dict:
    """
    Generates and fills out our payload.
    returns:
    payload (dict): account creation payload data
    """
    # Get username/password options from settings.ini
    email = get_settings_variables()[5]
    password = get_settings_variables()[6]

    if not email:  # We aren't using a custom username prefix -> make it random
        email = ''.join([random.choice(string.ascii_lowercase + string.digits)
                         for n in range(6)]) + '@gmail.com'
    else:  # We're using a custom prefix for our usernames
        email = email + str(random.randint(1000, 9999)) + '@gmail.com'

    if not password:
        password = email[:-10] + str(random.randint(1, 1000))

    # Generate random birthday for the account
    day = str(random.randint(1, 25))
    month = str(random.randint(1, 12))
    year = str(random.randint(1980, 2006))  # Be at least 13 years old

    payload = {
        'theme': 'oldschool',
        'email1': email,
        'onlyOneEmail': '1',
        'password1': password,
        'onlyOnePassword': '1',
        'day': day,
        'month': month,
        'year': year,
        'create-submit': 'create',
        'g-recaptcha-response': captcha
    }
    return payload


def check_account(submit):
    """Checks to make sure the account was successfully created"""
    submit_page = submit.text
    success = '<p>You can now begin your adventure with your new account.</p>'
    if success in submit_page:
        print("\nAccount was successfully created.\n")
        return True
    elif 'Warning!' in submit_page: # If account creation fails, print the error
        print("\nAccount was not created successfully - error below:")
        error_text = submit_page[get_index(submit_page, 'Warning!', 1)+23:]
        error_text = error_text[:get_index(error_text, '<', 1)]
        print(error_text)
        return False
    else:
        print("Account was not created successfully "
              "and we weren't able to catch the error.. ")
        return False



def save_account(payload, proxy=None):
    """Save the needed account information to created_accs.txt"""
    if USE_PROXIES:
        proxy_auth_type = get_settings_variables()[13]
        if proxy_auth_type == 1: # Formatting based on user:pass auth
            # Formatting our proxy string to only save the IP
            proxy = str(proxy)
            proxy = proxy[proxy.find('@')+1:]
            proxy = proxy[:proxy.find(':')]
        else: # Formatting based on IP authentication
            proxy = str(proxy)
            proxy = proxy[proxy.find('/')+2:]
            proxy = proxy[:proxy.find("'")]
    else:
        proxy = get_ip()

    # Check if we want user friendly formatting or bot manager friendly
    acc_details_format = get_settings_variables()[7]
    if acc_details_format:
        formatted_payload = (f"\nemail:{payload['email1']}, password:{payload['password1']},"
                             f" Birthday:{payload['month']}/{payload['day']}/{payload['year']},"
                             f" Proxy:{proxy}")
    else:
        formatted_payload = (f"\n{payload['email1']}:{payload['password1']}")

    with open("created_accs.txt", "a+") as acc_list:
        acc_list.write(formatted_payload)
    if acc_details_format:
        print(f"Created account and saved to created_accs.txt"
              f" with the following details:{formatted_payload}")
    else:
        print(f"Created account and saved to created_accs.txt"
              f" with the following details: {formatted_payload}:{proxy}")


def create_account():
    """Creates our account and returns the registration info"""
    requests.session()
    if USE_PROXIES:
        proxy = get_proxy()
        if access_page(proxy):
            payload = get_payload(captcha_solver())
            submit = requests.post(SITE_URL, data=payload, proxies=proxy)
            if submit.ok:
                # Debug message below because on a rare occasion,
                # An account wont create successfully and its not due to payload
                print(f"creation post status code: {submit.status_code} #debug")
                if check_account(submit):
                    save_account(payload, proxy=proxy)
                    if TRIBOT_ACTIVE:
                        use_tribot(payload['email1'], payload['password1'], proxy)
                    elif OSBOT_ACTIVE:
                        use_osbot(payload['email1'], payload['password1'], proxy)
                else:
                    print("We submitted our account creation request "
                          "but didn't get to the creation successful page. "
                          "Please send the error and status code to Gavin!")
                    print(submit.status_code)
            else:
                print(f"Creation failed. Error code {submit.status_code}")
    else:  # Not using proxies so we'll create the account(s) with our real IP
        if access_page():
            payload = get_payload(captcha_solver())
            submit = requests.post(SITE_URL, data=payload)
            if submit.ok:
                if check_account(submit):
                    save_account(payload)
                    if TRIBOT_ACTIVE:
                        use_tribot(payload['email1'], payload['password1'])
                    elif OSBOT_ACTIVE:
                        use_osbot(payload['email1'], payload['password1'])
                else:
                    print("We submitted our account creation request "
                          "but didn't get to the creation successful page. "
                          "Please send the error and status code to Gavin!")
                    print(submit.status_code)
            else:
                print(f"Creation failed. Error code {submit.status_code}")


def main():
    """Shows user info in the command line and runs the account creator"""
    counter = 0
    try:
        print(f"We'll make: {NUM_OF_ACCS} accounts.")
        print(f"Will we use proxies?: {USE_PROXIES}")
        print(f"Will we use Tribot CLI?: {TRIBOT_ACTIVE}")
        print(f"Will we use OSBot CLI?: {OSBOT_ACTIVE}\n")

        while counter < NUM_OF_ACCS:
            counter += 1
            create_account()
    except KeyboardInterrupt:
        print("User stopped the account creator.")


if __name__ == "__main__":
    main()
