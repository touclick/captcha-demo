#!coding:utf8
import sys

try:
    from setuptools import setup
except:
    from distutils.core import setup

if __name__ == "__main__":
    with open('requirements.txt') as f:
        required = f.read().splitlines()
    setup(
        name="touclick",
        version="1.2.0",
        packages=['captcha-python-sdk'],
        url='https://github.com/touclick/captcha-demo',
        license='',
        author='touclick',
        author_email='yuanshichao@touclick.com',
        description='Touclick Python SDK',
        install_requires=required,
    	)
