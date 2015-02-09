<?php

use Illuminate\Console\Command;
use Peekmo\JsonPath\JsonStore;

class SaveFloorInfoCommand extends Command {

    /**
     * The console command name.
     *
     * @var string
     */
    protected $name = 'command:name';

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
        Floor::query()->delete();

        $client = new CMXClient();
        $data   = $client->getFloorInfo();

        $store = new JsonStore($data);

        $floors = $store->get('$..Floor.*');

        $result = [];

        foreach ($floors as $floor) {
            $model = new Floor();

            $model->name   = $floor['name'];
            $model->width  = $floor['Dimension']['width'];
            $model->height = $floor['Dimension']['height'];
            $model->length = $floor['Dimension']['length'];
            $model->image  = asset('images/avanzi.jpg');
            $model->unit   = $floor['Dimension']['unit'];
            $model->save();

            foreach ($floor['Zone'] as $zone) {
                
            }

            

//            $insertData = [
//            'name' => $floor['name'],
//            'width' =>
//            'height' =>,
//            'length' =>
//            'image' =>
//            'unit' =>
//            'poi' => $floor['Zone']
//            ];
        }
    }

    /**
     * Get the console command arguments.
     *
     * @return array
     */
    protected function getArguments() {
        return array(
                //	array('example', InputArgument::REQUIRED, 'An example argument.'),
        );
    }

    /**
     * Get the console command options.
     *
     * @return array
     */
    protected function getOptions() {
        return array(
                //	array('example', null, InputOption::VALUE_OPTIONAL, 'An example option.', null),
        );
    }

}
