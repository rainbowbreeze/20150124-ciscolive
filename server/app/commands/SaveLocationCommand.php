<?php

use Illuminate\Console\Command;

class SaveLocationCommand extends Command {

    /**
     * The console command name.
     *
     * @var string
     */
    protected $name = 'locations:save';

    /**
     * The console command description.
     *
     * @var string
     */
    protected $description = 'Command description.';

    /**
     * Create a new command instance.
     *
     * @return void
     */
    public function __construct() {
        parent::__construct();
    }

    /**
     * Execute the console command.
     *
     * @return mixed
     */
    public function fire() {
        $manager = new LocationManager();

        $count = $manager->saveLocations();

        $this->info("{$count} locations saved");
    }

    /**
     * Get the console command arguments.
     *
     * @return array
     */
    protected function getArguments() {
        return array(
                //array('example', InputArgument::REQUIRED, 'An example argument.'),
        );
    }

    /**
     * Get the console command options.
     *
     * @return array
     */
    protected function getOptions() {
        return array(
                //array('example', null, InputOption::VALUE_OPTIONAL, 'An example option.', null),
        );
    }

}
