require 'sinatra'
require 'optparse'

set :port, 4321

delay = 0
if ENV['slow']
  delay = ENV['slow'].to_i
end

post_error = false
if ENV['post_error']
  post_error = true
end

heartbeat = File.read("heartbeat.json")
welcome = File.read("welcome.json")
profile = File.read("profile.json")
vehicles = File.read("vehicles.json")

define_method(:sleep_and_return) do |value|
  sleep delay
  value
end

get '/' do
  sleep_and_return "Hello World"
end

get '/api/v1/heartbeat.json' do
  sleep_and_return heartbeat
end

get '/api/v1/welcome.json' do
  sleep_and_return welcome
end

get '/api/v1/profile/profile.json' do
  sleep_and_return profile
end

get '/api/v1/profile/vehicles.json' do
  sleep_and_return vehicles
end

post '/api/v1/profile/addVehicle' do
  puts "POSTED NEW VEHICLE: #{params[:model]}"
  if post_error
    status 500
    body 'The server has laid an egg'
  else
    sleep_and_return "{}"
  end
end

