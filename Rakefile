
task :default => :compile

task :compile do 
  system 'javac solver/*.java'
end

task :test do
  system 'java solver.Main'
end

task :benchmark do
  system 'java solver.RunHardest20'
end
